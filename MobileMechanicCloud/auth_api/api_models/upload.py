from flask import jsonify, request
from flask_jwt import jwt_required
from flask_restful import Resource, abort
import os

class ImageUploadAPI(Resource):

    @jwt_required()
    def post(self, user_id, job_id):
        f = request.files['imagefile']
        print ('Received file')
        if f.filename == '':
            abort(404)
        else:
            filename = f.filename
            print ('os path is ', os.getcwd())
            if not os.path.exists('temp'):
                os.makedirs('temp')
            f.save(os.path.join('temp', filename))
            return jsonify({'success': 'Upload successful'})
