<?php

namespace App\Http\Controllers\Api;

use App\Alarm;
use Illuminate\Http\Request;

class AlarmController extends ApiController
{
    public function add(Request $request)
    {
        $this->validateRules($request, ['notification_time' => 'required|min:3|max:5']);

        $timeCheck = Alarm::where(['profile_id' => $this->profile->id, 'time' => $request->notification_time])->first();
        if ($timeCheck != null) {
            return response()->json(['success' => false, 'message' => 'This time was already added!'], 422);
        }

        $alarm = new Alarm;
        $alarm->profile_id = $this->profile->id;
        $alarm->time = $request->notification_time;
        $alarm->save();

        return response()->json(['success' => true, 'response' => ['alarm' => $alarm]], 200);
    }

    public function remove(Request $request)
    {
        $this->validateRules($request, ['id' => 'required|integer']);

        $timeCheck = Alarm::where(['profile_id' => $this->profile->id, 'id' => $request->id])->first();
        if ($timeCheck == null) {
            return response()->json(['success' => false, 'message' => 'This time was already removed!'], 200);
        }

        $timeCheck->delete();

        return response()->json(['success' => true, 'message' => 'Successfully removed your time!'], 200);
    }
}
