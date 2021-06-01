<?php

namespace App\Http\Controllers\API;

use Exception;
use App\Models\FoodPost;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Http;
use App\Helpers\ResponseFormatter;
use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Validator;

class FoodPostController extends Controller
{
    public function all(Request $request)
    {
        $id = $request->input('id');
        $id_user = $request->input('id_user');
        $food_name = $request->input('food_name');
        $category = $request->input('category');
        $location = $request->input('location');

        if($id)
        {
            $foodPost = FoodPost::with(['user'])->where('id', $id)->orderBy('created_at', 'desc')->get();

            if($foodPost) {
                return ResponseFormatter::success(
                    $foodPost,
                    'Data food post berhasil diambil'
                );
            }else{
                return ResponseFormatter::error(
                    null,
                    'Data food post tidak ada',
                    404
                );
            }
        }

        if($id_user)
        {
            $foodPost = FoodPost::query()->where('id_user', $id_user)->where('location', $location)->orderBy('created_at', 'desc')->get();

            if($foodPost) {
                return ResponseFormatter::success(
                    $foodPost,
                    'Data food post berhasil diambil'
                );
            }else{
                return ResponseFormatter::error(
                    null,
                    'Data food post tidak ada',
                    404
                );
            }
        }

        $foodPost = FoodPost::with(['user'])->where('location', $location)->where('is_verified', true)->orderBy('created_at', 'desc')->get();

        if ($food_name)
        {
            $foodPost = FoodPost::with(['user'])->where('food_name', 'like', '%' . $food_name . '%')->where('location', $location)->orderBy('created_at', 'desc')->get();
        }

        if ($category)
        {
            $foodPost = FoodPost::with(['user'])->where('category', 'like', '%' . $category . '%')->where('location', $location)->orderBy('created_at', 'desc')->get();
        }

        return ResponseFormatter::success(
            $foodPost,
            'Data list food post berhasil diambil'
        );
    }

    public function add(Request $request)
    {
        $is_verified = 0;

        $validator = Validator::make($request->all(), [
            'file' => 'required|image|max:2048',
        ]);

        if ($validator->fails()) {
            return ResponseFormatter::error(['error'=>$validator->errors()], 'Update Photo Fails', 401);
        }

        if ($request->file('file')) {
            $file = $request->file->store('assets/foodPost', 'public');
        }

        try {
            $user_id = Auth::user()->id;

            $request->validate([
                'food_name' => ['required', 'string', 'max:255'],
                'food_desc' => ['required', 'string', 'max:255'],
                'location' => 'required',
                'category' => 'required'
            ]);

            $newFood = FoodPost::create([
                'id_user' => $user_id,
                'taken_by' => null,
                'food_name' => $request->food_name,
                'food_desc' => $request->food_desc,
                'category' => $request->category,
                'location' => $request->location,
                'is_verified' => $request->is_verified,
                'is_available' => true,
                'picturePath' => $file
            ]);



            $foodPost = FoodPost::with('user')->find($newFood->id);

            return ResponseFormatter::success([
                'token_type' => 'Bearer',
                'id_user' => Auth::user()->id,
                'food_post' => $foodPost
            ],'Food post berhasil ditambahkan');
        } catch (Exception $e) { return ResponseFormatter::error($e->getMessage(), 'Food post tidak berhasil ditambahkan');}
    }

    public function update(Request $request, $id)
    {
        $foodPost = FoodPost::findOrFail($id);

        $foodPost->update($request->all());

        return ResponseFormatter::success($foodPost,'Food Post berhasil diperbarui');
    }


}
