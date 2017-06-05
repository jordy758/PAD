<?php

namespace App\Http\Controllers;

use App\Custom\Notification;
use App\Profile;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Session;

class NotificationController extends Controller
{
    public function send(Request $request)
    {
        $this->validate($request, [
            'receiver_first_name' => 'required|max:50',
            'receiver_last_name' => 'required|max:50',
            'notification_title' => 'required|max:50',
            'notification_body' => 'required|max:255'
        ]);
        $profile = Profile::where(
            ['first_name' => $request->receiver_first_name, 'last_name' => $request->receiver_last_name]
        )->first();

        $notification = new Notification($request->notification_title, $request->notification_body);
        $notification->send($profile);

        Session::flash('success', 'Successfully sent your notification!');
        return redirect()->back();
    }
}