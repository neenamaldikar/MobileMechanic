from flask import Flask
from flask_restful import Api
from flask_jwt import JWT, jwt_required, current_identity
from flask_pymongo import PyMongo


app = Flask(__name__)

app.config['MONGO_DBNAME'] = 'MobileMechanic'
mongo = PyMongo(app, config_prefix='MONGO')
with app.app_context():
    db = mongo.db

from authentication import authenticate, identity
app.config['SECRET_KEY'] = 'super-secret' # TODO: Change the secret_key
app.config['JWT_AUTH_URL_RULE'] = '/mobilemechanic/api/v1.0/auth'
jwt = JWT(app, authenticate, identity)

from users import UserAPI, ImageUploadAPI
api = Api(app)
#api.add_resource(UserListAPI, '/mobilemechanic/api/v1.0/users', endpoint ='users')
api.add_resource(UserAPI, '/mobilemechanic/api/v1.0/users/<int:user_id>', endpoint ='user')
api.add_resource(ImageUploadAPI, '/mobilemechanic/api/v1.0/users/<int:user_id>/job/<int:job_id>/picture')

if __name__ == '__main__':
    # changed reloader because there seemed to be an error while running in 
    # debug mode
    app.run(use_reloader=False)
