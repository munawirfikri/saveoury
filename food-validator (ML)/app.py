import flask
import io
import string
import time
import os
import numpy as np
import tensorflow as tf
from PIL import Image
from flask import Flask, jsonify, request

model = tf.keras.models.load_model('resnet50_food_model')

def prepare_image(img):
    img = Image.open(io.BytesIO(img))
    img = img.resize((224, 224))
    img = np.array(img)
    img = np.expand_dims(img, 0)
    return img


def predict_result(img):
    return 1 if model.predict(img)[0][0] > 0.5 else 0


app = Flask(__name__)

# Treat the web process
@app.route('/', methods=['GET', 'POST'])
def upload_file():
    if request.method == 'POST':
        if 'file' not in request.files:
            return "Please try again."
        
        file = request.files.get('file')
        
        if not file:
            return
        
        img_bytes = file.read()
        img = prepare_image(img_bytes)
        
        return jsonify(prediksi=predict_result(img))

    return 'Machine Learning Inference'


if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0')