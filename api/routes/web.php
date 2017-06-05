<?php

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| contains the "web" middleware group. Now create something great!
|
*/

Route::get('/', function () {
    return view('welcome');
});

Auth::routes();

Route::get('/home', 'HomeController@index');
Route::get('/profile', 'ProfileController@index');
Route::post('/notification/send', 'NotificationController@send');

Route::post('/api/profile/register', 'Api\ProfileController@register');
Route::post('/api/profile/get_all_information', 'Api\ProfileController@getAllInformation');

Route::post('/api/alarm/add', 'Api\AlarmController@add');
Route::post('/api/alarm/remove', 'Api\AlarmController@remove');

Route::post('/api/smoke_data/add', 'Api\SmokeDataController@add');
Route::post('/api/smoke_data/get_tile_data', 'Api\SmokeDataController@getTileData');

Route::post('/api/goal/add', 'Api\GoalController@add');
Route::post('/api/goal/remove', 'Api\GoalController@remove');
