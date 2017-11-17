class Mechanic:
    def __init__(self, user_id, phone_number=None, address_line1=None,
                 address_line2=None, city=None, state=None, zipcode=None, rate=None,
                 rating=None, reviews=None):
        self.user_id = user_id
        self.phone_number = phone_number
        self.address_line1 = address_line1
        self.address_line2 = address_line2
        self.city = city
        self.state = state
        self.zipcode = zipcode
        self.rate = rate
        self.rating = rating
        self.reviews = reviews
