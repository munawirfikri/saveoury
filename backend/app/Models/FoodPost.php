<?php

namespace App\Models;

use Carbon\Carbon;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;
use Illuminate\Support\Facades\Storage;

class FoodPost extends Model
{
    use HasFactory, SoftDeletes;

    protected $fillable = [
        'picturePath', 'id_user', 'category', 'location', 'food_name', 'food_desc', 'is_verified', 'is_available'
    ];

    public function user()
    {
        return $this->hasOne(User::class,'id','id_user');
    }

    public function user_taken()
    {
        return $this->hasOne(User::class,'id','taken_by');
    }

    public function toArray()
    {
        $toArray = parent::toArray();
        $toArray['picturePath'] = $this->picturePath;
        return $toArray;
    }

    public function getCreatedAtAttribute($created_at)
    {
        return Carbon::parse($created_at)
            ->getPreciseTimestamp(3);
    }
    public function getUpdatedAtAttribute($updated_at)
    {
        return Carbon::parse($updated_at)
            ->getPreciseTimestamp(3);
    }
    public function getPicturePathAttribute()
    {
        return url('') . Storage::url($this->attributes['picturePath']);
    }
}
