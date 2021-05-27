<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateFoodPostsTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('food_posts', function (Blueprint $table) {
            $table->id();

            $table->integer('id_user');
            $table->integer('taken_by')->nullable();

            $table->string('food_name')->nullable();
            $table->string('food_desc')->nullable();
            $table->string('category')->nullable();
            $table->string('location')->nullable();
            $table->boolean('is_verified')->default(false);
            $table->boolean('is_available')->default(false);

            $table->text('picturePath')->nullable();
            $table->softDeletes();
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('food_posts');
    }
}
