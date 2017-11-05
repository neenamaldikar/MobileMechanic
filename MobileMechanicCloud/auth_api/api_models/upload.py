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

class ImageUploadAPI(Resource):
    def __init__(self):
        self.jobsDAO = JobRequestDAO(mongo)
        self.reqparse = reqparse.RequestParser()
        self.reqparse.add_argument('picture_id', type=str, location='args')
    #
    # assuming you already have a job id
    # TODO: add a check for job_id
    @jwt_required()
    def post(self, user_id, job_id):
        user_id = str(user_id)
        if user_id == current_identity._get_current_object().id:
            # files = request.files.getlist('imagesfiles[]')
            f = request.files.get('imagefile')
            print ('Started processing files')
            # print ('Files are', f.filena)
            # for f in files:
            print ('Current file is', f.filename)
            if f.filename == '':
                abort(404)
            content_type = f.content_type
            with tempfile.NamedTemporaryFile() as fp:
                f.save(fp.name)
                fp.seek(0, 0)
                image_data = Binary(fp.read())
            update_result = self.jobsDAO.update_images(user_id, job_id, image_data,
                                                  f.filename, content_type)
            if update_result:
                result = self.jobsDAO.find_job(user_id, job_id)
                print ('result is', result.__dict__)
                return jsonify(results=str(result.__dict__))
            else:
                abort(404)
                # return jsonify({'success': 'Upload successful'})

    # TODO: add the image names
    @jwt_required()
    def get(self, user_id, job_id):
        user_id = str(user_id)
        if user_id == current_identity._get_current_object().id:
            args = self.reqparse.parse_args()
            picture_id = args.get('picture_id')
            if picture_id:
                image_data = self.jobsDAO.get_image(user_id, job_id, picture_id)
                if image_data:
                    print ('Got image data')
                    return send_file(io.BytesIO(image_data), mimetype='image/png')
                else:
                    abort(401)
            else:
                abort(401)
        else:
            abort(401)
