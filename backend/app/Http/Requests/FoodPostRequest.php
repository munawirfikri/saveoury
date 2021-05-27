<?php

namespace App\Http\Requests;

use Illuminate\Foundation\Http\FormRequest;

class FoodPostRequest extends FormRequest
{
    /**
     * Determine if the user is authorized to make this request.
     *
     * @return bool
     */
    public function authorize()
    {
        return true;
    }

    /**
     * Get the validation rules that apply to the request.
     *
     * @return array
     */
    public function rules()
    {
        return [
            'food_name' => 'required|max:255',
            'picturePath' => 'required|image',
            'food_desc' => 'required',
            'id_user' => 'required',
            'taken_by' => '',
            'is_verified' => 'required|integer',
            'is_available' => 'required|integer',
        ];
    }
}
