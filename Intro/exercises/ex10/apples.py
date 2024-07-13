import game_utils


class Apple:
    """
    init- creating new apple object with location(tuple[int,int])
    get_loc - return the location of the apple
    """

    def __init__(self):
        self.__loc = game_utils.get_random_apple_data()

    def get_loc(self):
        return self.__loc
