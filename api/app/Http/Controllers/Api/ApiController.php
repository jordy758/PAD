<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Profile;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;

abstract class ApiController extends Controller
{
    /** @var Profile $profile */
    protected $profile;

    public function validateRules(Request $request, $extraRules = null, $validateProfile = true)
    {
        $rules = ['notification_token' => 'required|min:150|max:155'];
        if ($extraRules) {
            $rules = array_merge($rules, $extraRules);
        }

        $validation = Validator::make($request->all(), $rules);

        if (!$validation->passes()) {
            return response()->json(
                ['success' => false, 'message' => $validation->errors()],
                422
            )->throwResponse();
        }

        $this->profile = Profile::whereNotificationToken($request->notification_token)->first();

        if (!$this->profile && $validateProfile) {
            return response()->json(
                ['success' => false, 'message' => 'Profile not found with this token'],
                401
            )->throwResponse();
        }
    }
}