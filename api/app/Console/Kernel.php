<?php

namespace App\Console;

use App\Alarm;
use App\Custom\Notification;
use App\Profile;
use App\SavingGoal;
use App\SmokeData;
use Carbon\Carbon;
use Illuminate\Console\Scheduling\Schedule;
use Illuminate\Foundation\Console\Kernel as ConsoleKernel;
use Illuminate\Support\Facades\DB;

class Kernel extends ConsoleKernel
{
    /**
     * The Artisan commands provided by your application.
     *
     * @var array
     */
    protected $commands = [
        //
    ];

    /**
     * Define the application's command schedule.
     *
     * @param  \Illuminate\Console\Scheduling\Schedule $schedule
     * @return void
     */
    protected function schedule(Schedule $schedule)
    {
        // Send notifications based on time
        $schedule->call(function () {
            $notifications = Alarm::where(['time' => date('H:i')])->get();

            foreach ($notifications as $notification) {
                $profile = $notification->profile;

                $notification = new Notification(
                    'Laten we weer beginnen!',
                    '{first_name} het is weer tijd om je gegevens in te vullen!'
                );
                $notification->send($profile);
            }
        })->everyMinute();

        // Check if goals are reached
        $schedule->call(function () {
            $smokeData = DB::select(
                'SELECT SUM(amount) AS cigarettes_smoked, profile_id, cigarettes_per_pack, price_per_pack, 
                cigarettes_per_day, datediff(MAX(time_smoked), MIN(time_smoked)) + 1 AS days_inbetween 
            FROM smoke_data 
            JOIN profiles ON profiles.id = smoke_data.profile_id
            WHERE added_to_price = 0 AND date(time_smoked) <= date(CURRENT_DATE - INTERVAL 1 DAY) 
            GROUP BY profile_id'
            );

            /** @var SmokeData $smoke */
            foreach ($smokeData as $smoke) {
                $profile = Profile::find($smoke->profile_id);

                $cigarettesSaved = ($smoke->cigarettes_per_day * $smoke->days_inbetween) - $smoke->cigarettes_smoked;
                $savedAmount = $cigarettesSaved * ($profile->price_per_pack / $profile->cigarettes_per_pack);

                $profile->saved_amount = $profile->saved_amount + $savedAmount;
                $profile->save();

                DB::table('smoke_data')
                    ->where('profile_id', $profile->id)
                    ->where('added_to_price', 0)
                    ->whereDate('time_smoked', '<=', Carbon::now()->addDays(-1))
                    ->update(['added_to_price' => 1]);

                $firstGoal = SavingGoal::whereProfileId($smoke->profile_id)
                    ->whereNull('achieved_at')
                    ->orderBy('id')
                    ->first();

                if ($firstGoal && $profile->saved_amount >= $firstGoal->price) {
                    $profile->saved_amount = $profile->saved_amount - $firstGoal->price;
                    $profile->save();

                    $firstGoal->achieved_at = Carbon::now();
                    $firstGoal->save();

                    // Send notification
                    $notification = new Notification(
                        "Goed bezig! Doel behaalt!",
                        "{first_name} je hebt jou doel: {$firstGoal->goal} behaald!");
                    $notification->send($profile);
                }
            }
        })->dailyAt('11:00');
    }

    /**
     * Register the Closure based commands for the application.
     *
     * @return void
     */
    protected function commands()
    {
        require base_path('routes/console.php');
    }
}
