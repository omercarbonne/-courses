def is_vormir_safe(temperature, measurement1, measurement2, measurement3):
    """This function gets a temperature and the measurements of the temperatures on the last 3 days,
     and return True if at least 2 days temperatures were higher then the one that given"""
    num_worm_days = 0
    if measurement1 > temperature:
        num_worm_days = num_worm_days + 1
    if measurement2 > temperature:
        num_worm_days = num_worm_days + 1
    if measurement3 > temperature:
        num_worm_days = num_worm_days + 1
    if num_worm_days > 1:
        return True
    else:
        return False

