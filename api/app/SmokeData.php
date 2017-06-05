<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

/**
 * App\SmokeData
 *
 * @property-read \App\Profile $profile
 * @mixin \Eloquent
 * @property int $id
 * @property string $time_smoked
 * @property int $amount
 * @property int $profile_id
 * @method static \Illuminate\Database\Query\Builder|\App\SmokeData whereAmount($value)
 * @method static \Illuminate\Database\Query\Builder|\App\SmokeData whereId($value)
 * @method static \Illuminate\Database\Query\Builder|\App\SmokeData whereProfileId($value)
 * @method static \Illuminate\Database\Query\Builder|\App\SmokeData whereTimeSmoked($value)
 * @property bool $added_to_price
 * @method static \Illuminate\Database\Query\Builder|\App\SmokeData whereAddedToPrice($value)
 */
class SmokeData extends Model
{
    public $timestamps = false;
    protected $table = 'smoke_data';
    protected $dates = ['time_smoked'];

    public function profile()
    {
        return $this->belongsTo('App\Profile');
    }
}
