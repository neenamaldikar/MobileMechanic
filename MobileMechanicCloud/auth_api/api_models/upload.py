from flask import jsonify, request
from flask_jwt import jwt_required, current_identity
from flask_restful import Resource, abort
import os

class ImageUploadAPI(Resource):

    @jwt_required()
    def post(self, user_id, job_id):
        user_id = str(user_id)
        current_user = str(current_identity._get_current_object().id.encode('utf-8'), encoding='utf-8')
        if user_id == current_user:
            f = request.files['imagefile']
            print ('Received file')
            if f.filename == '':
                abort(404)
            else:
                filename = f.filename
                print ('OS path is ', os.getcwd())
                if not os.path.exists('imagestorage'):
                    os.makedirs('imagestorage')
                print ('User is', current_user)
                updated_path = os.path.join('imagestorage', user_id)
                if not os.path.exists(updated_path):
                    os.makedirs(updated_path)
                updated_path = os.path.join(updated_path, str(job_id))
                if not os.path.exists(updated_path):
                    os.makedirs(updated_path)
                updated_path = os.path.join(updated_path, filename)
                f.save(updated_path)
                return jsonify({'success': 'Upload successful'})
