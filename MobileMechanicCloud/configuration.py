import os
import datetime
import sys

LOGGING_JSON = {
    'version': 1,
    'disable_existing_loggers': False,
    'formatters': {
        'standard': {
            'format': '%(asctime)s - %(name)s - %(levelname)s: %(message)s'
        },
    },
    'handlers': {
        'console': {
            'level':'DEBUG',
            'class':'logging.StreamHandler',
            'stream': sys.stdout,
            'formatter': 'standard'
        }
    },
    'loggers': {
        '': {
            'handlers': ['console'],
            'level': 'DEBUG'
        }
    }
}

# test the loggers over here
# logging.config.dictConfig(LOGGING_JSON)

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
    MONGO_URI = os.getenv('MONGO_URI', '')
    FCM_SERVER_KEY = os.getenv('FCM_SERVER_KEY', '')

class ProductionConfig:
    DEBUG = False
    FB_APP_ID = os.getenv('FB_APP_ID', '')
    FB_APP_SECRET = os.getenv('FB_APP_SECRET', '')
    FB_APP_NAME = os.getenv('FB_APP_NAME', '')
    SECRET_KEY = os.getenv('APP_SECRET_KEY', '')
    JWT_AUTH_URL_RULE = os.getenv('JWT_AUTH_RULE', '')
    JWT_EXPIRATION_DELTA = datetime.timedelta(minutes=15)
    MONGO_URI = os.getenv('MONGO_URI', '')
    FCM_SERVER_KEY = os.getenv('FCM_SERVER_KEY', '')