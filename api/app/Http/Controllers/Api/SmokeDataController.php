<?php

namespace App\Http\Controllers\Api;

use App\Custom\TileData;
use App\SmokeData;
use Carbon\Carbon;
use Illuminate\Http\Request;

class SmokeDataController extends ApiController
{
    public function getTileData(Request $request)
    {
        $this->validateRules($request);

        $tileData = new TileData();

        $amount = SmokeData::whereProfileId($this->profile->id)
            ->whereDay('time_smoked', '=', date('d'))
            ->sum('amount');

        $lastSmokeData = SmokeData::whereProfileId($this->profile->id)
            ->whereDay('time_smoked', '!=', date('d'))->orderByDesc('time_smoked')
            ->first();

        if ($lastSmokeData) {
            $daysTryingToStop = $this->profile->created_at->diffInDays($lastSmokeData->time_smoked);
        } else {
            $daysTryingToStop = 0;
        }

        $perfectLine = [];
        $daysTillStopDate = $this->profile->created_at->diffInDays($this->profile->stop_date);
        $stopPerDay = $this->profile->cigarettes_per_day / $daysTillStopDate;
        $current = $this->profile->cigarettes_per_day;

        $desiredAmount = $this->profile->cigarettes_per_day;
        for ($i = 1; floor($current) != 0; $i++) {
            $current -= $stopPerDay;
            $perfectLine[] = floor($current);

            if ($i == $daysTryingToStop) {
                $desiredAmount = floor($current);
            }
        }

        $myLine = [];
        $reference = $this->profile->created_at;
        for ($i = 1; $i <= $daysTryingToStop; $i++) {
            $reference->addDays(1);
            $amountNow = SmokeData::whereProfileId($this->profile->id)
                ->whereDate('time_smoked', '=', $reference->toDateString())
                ->sum('amount');

            if ($amountNow) {
                $myLine[] = (int)$amountNow;
            } else {
                $myLine[] = $this->profile->cigarettes_per_day;
            }
        }

        $tileData->myLine = $myLine;
        $tileData->perfectLine = $perfectLine;
        $tileData->smokedToday = $amount;
        $tileData->cigarettesSaved = $desiredAmount - $amount;
        $tileData->setSavedMoney(
            ($this->profile->cigarettes_per_day - $amount) * ($this->profile->price_per_pack / $this->profile->cigarettes_per_pack)
        );
        $tileData->notSmokedFor = "No data found!";

        $lastSmokeData = SmokeData::whereProfileId($this->profile->id)
            ->where('amount', '!=', 0)
//            ->whereDay('time_smoked', '=', date('d'))
            ->orderBy('time_smoked', 'desc')->first();

        if ($lastSmokeData) {
            $lastSmokeDate = Carbon::parse($lastSmokeData->time_smoked);

            $hours = $lastSmokeDate->diffInHours(Carbon::now());
            $minutes = $lastSmokeDate->diffInMinutes(Carbon::now()) - ($hours * 60);
            $date = sprintf('%02d', $hours) . ':' . sprintf('%02d', $minutes);
            $tileData->notSmokedFor = $date;
        }

        return response()->json(['success' => true, 'message' => $tileData]);
    }

    public function add(Request $request)
    {
        $this->validateRules($request, ['amount' => 'required|integer|digits_between:0,50']);
        $smokeData = new SmokeData;
        $smokeData->time_smoked = Carbon::now();
        $smokeData->amount = $request->amount;
        $smokeData->profile_id = $this->profile->id;
        $smokeData->added_to_price = false;
        $smokeData->save();

        return response()->json(['success' => true, 'response' => ['smoke_data' => $smokeData]], 200);
    }
}
