# TODO: make the id a private field only accessible by the current_identity module
# for the above, modifying the __dict__ method will probably help
# TODO: check if we need the last_seen field
# TODO: add validations

class User:
    def __init__(self, user_id, first_name, last_name, email, gender, last_seen,
                 address_line=None, city=None, state=None, zipcode=None, phone_number=None):
        # we get the first 6 fields during the first time authentication of the user
        # if a user is deleted, we have to make them login again to restore their access
        self.id = user_id  # used only by the current_identity module in jwt
        self.user_id = user_id
        self.first_name = first_name
        self.last_name = last_name
        self.email = email
        self.gender = gender
        self.last_seen = last_seen
        self.phone_number = phone_number
        self.address_line = address_line
        self.city = city
        self.state = state
        self.zipcode = zipcode

    def __str__(self):
        return "User(user_id='{0}')".format(self.user_id)

    def as_dict(self):
        return {
            'user_id': self.user_id,
            'first_name': self.first_name,
            'last_name': self.last_name,
            'email': self.email,
            'gender': self.gender,
            # 'last_seen': self.last_seen,
            'phone_number': self.phone_number,
            'address_line': self.address_line,
            'city': self.city,
            'state': self.state,
            'zipcode': self.zipcode
        }