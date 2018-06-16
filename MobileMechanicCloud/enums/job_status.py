from enum import Enum

class JobStatus(Enum):
    submitted = 1
    quotes_available = 2
    quote_accepted = 3
    in_progress = 4
    complete = 5