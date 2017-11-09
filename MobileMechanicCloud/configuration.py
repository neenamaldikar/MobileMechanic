# FB App details
#APP_ID = '1894402924175386' # Neil: '811832542261786'
#APP_SECRET = '5b26f12f47760bfed7fbf05467c3168c' #Neil: '55c624f94ba89d8be9f55ab601c6cf6c'
#APP_NAME = 'MobileMechanic'
import os
import datetime

class DevelopmentConfig:
    DEBUG = True
    FB_APP_ID = os.getenv('FB_APP_ID', '')
    FB_APP_SECRET = os.getenv('FB_APP_SECRET', '')
    FB_APP_NAME = os.getenv('FB_APP_NAME', '')
    SECRET_KEY = os.getenv('APP_SECRET_KEY', '')
    JWT_AUTH_URL_RULE = os.getenv('JWT_AUTH_RULE', '')
    JWT_EXPIRATION_DELTA = datetime.timedelta(minutes=60)
    # if we work with multiple databases, we'll need to use the
    # configuration prefix for each of the databases
    MONGO_URI = os.getenv('MONGO_URI')
