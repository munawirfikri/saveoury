<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\API\UserController;
use App\Http\Controllers\API\FoodPostController;
use App\Http\Controllers\API\TransactionController;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/


Route::middleware(['auth:sanctum'])->group(function () {
    Route::get('user', [UserController::class, 'fetch']);
    Route::post('user', [UserController::class, 'updateProfile']);
    Route::post('user/photo', [UserController::class, 'updatePhoto']);

    Route::post('foodpost/add', [FoodPostController::class, 'add']);
    Route::post('foodpost/update/{id}', [FoodPostController::class, 'update']);
    Route::post('transaction/add', [TransactionController::class, 'add']);
    Route::post('transaction/update/{id}', [TransactionController::class, 'update']);
    Route::post('logout', [UserController::class, 'logout']);
});

Route::get('transaction', [TransactionController::class, 'all']);
Route::get('foodpost', [FoodPostController::class, 'all']);
Route::post('login', [UserController::class, 'login']);
Route::post('register', [UserController::class, 'register']);

