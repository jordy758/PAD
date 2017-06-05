<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

/**
 * App\Alarm
 *
 * @property-read \App\Profile $profile
 * @mixin \Eloquent
 * @property int $id
 * @property int $profile_id
 * @property string $time
 * @property \Carbon\Carbon $created_at
 * @property \Carbon\Carbon $updated_at
 * @method static \Illuminate\Database\Query\Builder|\App\Alarm whereCreatedAt($value)
 * @method static \Illuminate\Database\Query\Builder|\App\Alarm whereId($value)
 * @method static \Illuminate\Database\Query\Builder|\App\Alarm whereProfileId($value)
 * @method static \Illuminate\Database\Query\Builder|\App\Alarm whereTime($value)
 * @method static \Illuminate\Database\Query\Builder|\App\Alarm whereUpdatedAt($value)
 */
class Alarm extends Model
{
    public function profile()
    {
        return $this->belongsTo('App\Profile');
    }
}
