<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

/**
 * App\Profile
 *
 * @property-read \Illuminate\Database\Eloquent\Collection|\App\SavingGoal[] $savingGoals
 * @property-read \Illuminate\Database\Eloquent\Collection|\App\SmokeData[] $smokeData
 * @property-read \Illuminate\Database\Eloquent\Collection|\App\Alarm[] $times
 * @mixin \Eloquent
 * @property int $id
 * @property string $first_name
 * @property string $last_name
 * @property string $birth_date
 * @property string $notification_token
 * @property \Carbon\Carbon $created_at
 * @property \Carbon\Carbon $updated_at
 * @property int $cigarettes_per_pack
 * @property int $cigarettes_per_day
 * @property float $price_per_pack
 * @method static \Illuminate\Database\Query\Builder|\App\Profile whereBirthDate($value)
 * @method static \Illuminate\Database\Query\Builder|\App\Profile whereCigarettesPerDay($value)
 * @method static \Illuminate\Database\Query\Builder|\App\Profile whereCigarettesPerPack($value)
 * @method static \Illuminate\Database\Query\Builder|\App\Profile whereCreatedAt($value)
 * @method static \Illuminate\Database\Query\Builder|\App\Profile whereFirstName($value)
 * @method static \Illuminate\Database\Query\Builder|\App\Profile whereId($value)
 * @method static \Illuminate\Database\Query\Builder|\App\Profile whereLastName($value)
 * @method static \Illuminate\Database\Query\Builder|\App\Profile whereNotificationToken($value)
 * @method static \Illuminate\Database\Query\Builder|\App\Profile wherePricePerPack($value)
 * @method static \Illuminate\Database\Query\Builder|\App\Profile whereUpdatedAt($value)
 * @property float $saved_amount
 * @method static \Illuminate\Database\Query\Builder|\App\Profile whereSavedAmount($value)
 * @property-read \Illuminate\Database\Eloquent\Collection|\App\Alarm[] $alarms
 * @property \Carbon\Carbon $stop_date
 * @method static \Illuminate\Database\Query\Builder|\App\Profile whereStopDate($value)
 */
class Profile extends Model
{
    protected $dates = ['stop_date'];

    public function alarms()
    {
        return $this->hasMany('App\Alarm');
    }

    public function smokeData()
    {
        return $this->hasMany('App\SmokeData');
    }

    public function savingGoals()
    {
        return $this->hasMany('App\SavingGoal');
    }
}
