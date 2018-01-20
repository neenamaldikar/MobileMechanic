class Token:
    def __init__(self, user_id, token):
        self.user_id = user_id
        self.token = token

    def __str__(self):
        return "Token(user_id='{0}, token={1}')".format(self.user_id, self.token)

    def as_dict(self):
        return {
            'user_id': self.user_id,
            'token': self.token
        }