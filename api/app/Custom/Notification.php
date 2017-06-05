<?php

namespace App\Custom;

use App\Profile;
use LaravelFCM\Facades\FCM;
use LaravelFCM\Message\OptionsBuilder;
use LaravelFCM\Message\PayloadDataBuilder;
use LaravelFCM\Message\PayloadNotificationBuilder;

class Notification
{
    protected $title;
    protected $message;

    public function __construct($title, $message)
    {
        $this->title = $title;
        $this->message = $message;
    }

    public function send(Profile $profile)
    {
        $this->parse($profile);

        $optionBuiler = new OptionsBuilder();
        $optionBuiler->setTimeToLive(60 * 20);

        $notificationBuilder = new PayloadNotificationBuilder();

        $dataBuilder = new PayloadDataBuilder();
        $dataBuilder->addData(['title' => $this->title, 'text' => $this->message]);

        $option = $optionBuiler->build();
        $notification = $notificationBuilder->build();
        $data = $dataBuilder->build();

        FCM::sendTo($profile->notification_token, $option, $notification, $data);
    }

    protected function parse(Profile $profile)
    {
        $this->title = str_replace('{first_name}', $profile->first_name, $this->title);
        $this->title = str_replace('{last_name}', $profile->last_name, $this->title);

        $this->message = str_replace('{first_name}', $profile->first_name, $this->message);
        $this->message = str_replace('{last_name}', $profile->last_name, $this->message);
    }
}
