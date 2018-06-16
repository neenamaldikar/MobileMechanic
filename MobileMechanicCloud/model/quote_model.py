# TODO: add a few more validations to the fields

class QuoteRequest:
    def __init__(self, job_id, customer_user_id, mechanic_user_id, quote_id, labor_cost, part_cost,
                     onsite_service_charges, comments, status):
        self.job_id = job_id
        self.customer_user_id = customer_user_id
        self.mechanic_user_id = mechanic_user_id
        self.quote_id = quote_id
        self.labor_cost = labor_cost
        self.part_cost = part_cost
        self.onsite_service_charges = onsite_service_charges
        self.comments = comments
        self.status = status

    def __str__(self):
        return "Quote(job_id='{0}', customer_user_id='{1}', mechanic_user_id='{2}',quote_id='{3}')"\
            .format(self.job_id, self.customer_user_id, self.mechanic_user_id, self.quote_id)

    def as_dict(self):
        return {
            'job_id': self.job_id,
            'customer_user_id': self.customer_user_id,
            'mechanic_user_id': self.mechanic_user_id,
            'quote_id': self.quote_id,
            'labor_cost': self.labor_cost,
            'part_cost': self.part_cost,
            'onsite_service_charges': self.onsite_service_charges,
            'comments': self.comments,
            'status': self.status
        }