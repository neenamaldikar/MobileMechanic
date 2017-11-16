from flask import jsonify, request, send_file
from flask_jwt import jwt_required, current_identity
from flask_restful import Resource, abort, reqparse
import os
from extensions import mongo
from database.job_request import JobRequestDAO
from bson import Binary
from gridfs import GridFS
import tempfile
import io
from PIL import Image

class ImageUploadAPI(Resource):
    def __init__(self):
        self.jobsDAO = JobRequestDAO(mongo)
        self.reqparse = reqparse.RequestParser()
        self.reqparse.add_argument('picture_id', type=str, location='args')

    # TODO: add a check for job_id
    @jwt_required()
    def post(self, user_id, job_id):
        user_id = str(user_id)
        if user_id == current_identity._get_current_object().id:
            f = request.files.get('imagefile')
            if not f:
                abort(401)
            if f.filename == '':
                abort(401)
            content_type = f.content_type
            # with tempfile.NamedTemporaryFile() as fp:
            #     f.save(fp.name)
            #     fp.seek(0, 0)
            image_data = Binary(f.stream.read())
            update_result = self.jobsDAO.update_images(user_id, job_id, image_data,
                                                  f.filename, content_type)
            if update_result:
                result = self.jobsDAO.find_job(user_id, job_id)
                return jsonify(results=result.__dict__)
            else:
                abort(401)

    # converts all images to JPEG as response
    @jwt_required()
    def get(self, user_id, job_id):
        user_id = str(user_id)
        if user_id == current_identity._get_current_object().id:
            args = self.reqparse.parse_args()
            picture_id = args.get('picture_id')
            if picture_id:
                image_data = self.jobsDAO.get_image(user_id, job_id, picture_id)
                if image_data:
                    pil_image = Image.open(io.BytesIO(image_data)).convert('RGB')
                    temp_img_io = io.BytesIO()
                    pil_image.save(temp_img_io, 'JPEG', quality=70)
                    temp_img_io.seek(0)
                    return send_file(temp_img_io, mimetype='image/jpeg')
                else:
                    abort(401)
            else:
                abort(401)
        else:
            abort(401)
