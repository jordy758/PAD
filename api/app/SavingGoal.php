<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

/**
 * App\SavingGoal
 *
 * @property-read \App\Profile $profile
 * @mixin \Eloquent
 * @property int $id
 * @property string $goal
 * @property float $price
 * @property int $profile_id
 * @method static \Illuminate\Database\Query\Builder|\App\SavingGoal whereGoal($value)
 * @method static \Illuminate\Database\Query\Builder|\App\SavingGoal whereId($value)
 * @method static \Illuminate\Database\Query\Builder|\App\SavingGoal wherePrice($value)
 * @method static \Illuminate\Database\Query\Builder|\App\SavingGoal whereProfileId($value)
 * @property string $achieved_at
 * @method static \Illuminate\Database\Query\Builder|\App\SavingGoal whereAchievedAt($value)
 */
class SavingGoal extends Model
{
    public $timestamps = false;

    public function profile()
    {
        return $this->belongsTo('App\Profile');
    }
}
