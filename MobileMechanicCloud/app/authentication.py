import facebook
# added so that we can run the code from the terminal
import os, sys
sys.path.append(os.path.join(os.path.dirname(__file__), '..'))
from DAO.usersDAO import UsersDAO
from app import db
from DTO.user import User
usersDAO = UsersDAO(db)

def authenticate(username, password):
    profile, token_app_id, token_application = get_info_from_token(username)
    if profile is None:
        return
    if is_appinfo_valid(token_app_id, token_application) == False:
        return

    user = usersDAO.find_user(profile.get('id'))
    if user is None:
        usersDAO.insert_user(profile.get('id'), profile.get('first_name'),
                             profile.get('last_name'),profile.get('email'))
        user = usersDAO.find_user(profile.get('id'))

    return user


def is_appinfo_valid(token_app_id, token_application):
    if token_app_id.encode('utf8') == os.getenv('APP_ID') and token_application.encode('utf8') == os.getenv('APP_NAME'):
        return True
    else:
        return False


def get_info_from_token(username):
    try:
        graph = facebook.GraphAPI(access_token=username)
    except facebook.GraphAPIError:
        return

    print ('username is', username)
    print ('APP_ID is ', os.getenv('APP_ID'), 'APP_SECRET', os.getenv('APP_SECRET'))
    token_info = graph.debug_access_token(token=username,
                                          app_id=os.getenv('APP_ID'),
                                          app_secret=os.getenv('APP_SECRET'))

    #valid = token_info.get('data').get('is_valid')

    if token_info.get('data').get('is_valid'):
        token_app_id = token_info.get('data').get('app_id')
        token_application = token_info.get('data').get('application')
        args = {'fields': 'id,first_name,last_name,email'}
        # profile=graph.get_object('me')
        profile = graph.get_object('me', **args)
    else:
        profile = token_app_id = token_application = None

    return profile, token_app_id, token_application

def identity(payload):
    current_user = usersDAO.find_user(payload['identity'])
    return current_user
