<?php

namespace App\Http\Controllers\API;

use App\Helpers\ResponseFormatter;
use App\Http\Controllers\Controller;
use App\Models\Transaction;
use Exception;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;

class TransactionController extends Controller
{
    public function all(Request $request)
    {
        $id = $request->input('id');
        $food_id = $request->input('food_id');
        $owner_id = $request->input('owner_id');
        $status = $request->input('status');

        // by id transaksi
        if($id)
        {
            $transaction = Transaction::with(['food_post','user'])->find($id)->get();

            if($transaction){
                return ResponseFormatter::success(
                    $transaction,
                    'Data transaksi berhasil diambil'
                );
            }
            else{
                return ResponseFormatter::error(
                    null,
                    'Data transaksi tidak ada',
                    404
                );
            }
        }

        // By Id user
        $transaction = Transaction::with(['food_post','user'])->where('owner_id', $owner_id)->get();

        // By id user + food id
        if($food_id){
            $transaction = Transaction::with(['food_post','user'])->where('owner_id', $owner_id)->where('food_id', $food_id)->get();
        }

        // by id user + status
        if($status){
            $transaction = Transaction::with(['food_post','user'])->where('owner_id', $owner_id)->where('status', $status)->get();
        }
        return ResponseFormatter::success(
            $transaction,
            'Data list transaksi berhasil diambil'
        );
    }

        /**
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function add(Request $request)
    {
        $request->validate([
            'food_id' => 'required|exists:food_posts,id',
            'owner_id' => 'required|exists:users,id',
            'recipient_id' => 'required|exists:users,id',
            'status' => 'required',
        ]);

        $transaction = Transaction::create([
            'food_id' => $request->food_id,
            'owner_id' => $request->owner_id,
            'recipient_id' => $request->recipient_id,
            'status' => $request->status,
        ]);


        $transaction = Transaction::with(['food_post','user'])->find($transaction->id)->get();

        try {
            return ResponseFormatter::success($transaction,'Transaksi berhasil');
        }
        catch (Exception $e) {
            return ResponseFormatter::error($e->getMessage(),'Transaksi Gagal');
        }
    }

    /**
     * @param Request $request
     * @param $id
     * @return \Illuminate\Http\JsonResponse
     */
    public function update(Request $request, $id)
    {
        $transaction = Transaction::findOrFail($id);
        $user_id = Auth::user()->id;

        if ($user_id == $transaction->owner_id){
            $transaction->update($request->all());
            return ResponseFormatter::success($transaction,'Transaksi berhasil diperbarui');
        }else{
            return ResponseFormatter::error(
                null,
                'Data transaksi tidak berhasil diperbarui'
            );
        }
    }
}
