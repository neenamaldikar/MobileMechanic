class Mechanic:
    def __init__(self, user_id, phone_number, address_line1,
                 address_line2, city, state, zipcode, rate,
                 ratings, reviews):
        self.user_id = user_id
        self.phone_number = phone_number
        self.address_line1 = address_line1
        self.address_line2 = address_line2
        self.city = city
        self.state = state
        self.zipcode = zipcode
        self.rate = rate
        self.ratings = ratings
        self.reviews = reviews
