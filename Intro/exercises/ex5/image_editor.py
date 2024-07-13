#################################################################
# FILE : image_editor.py
# WRITER : omer carbonne , omercarbonne , your_id
# EXERCISE : intro2cs ex5 2022-2023
# DESCRIPTION: A simple program that
# STUDENTS I DISCUSSED THE EXERCISE WITH: Bugs Bunny, b_bunny.
#								 	      Daffy Duck, duck_daffy.
# WEB PAGES I USED: www.looneytunes.com/lola_bunny
# NOTES: ...
#################################################################

from ex5_helper import *
import math
import sys


OPTIONS_MESSAGE = """ Hello! choose edit option:
1 - Change colored image to grayscale image
2 - Blur image
3 - Change the size of the image
4 - Rotate image 90 degrees
5 - Show only edges
6 - Quantize image
7 - show image
8 - Exit
"""
ENTER_KERNEL_MESSAGE = "Enter the size of kernel, it has to be un even:  "
KERNEL_ERROR_MESSAGE = "You entered wrong number, try again"
ENTER_SIZE_OF_IMAGE_MESSAGE = "Enter the new size of the image, separated with ',' e.g: '300,400' )"
NEW_SIZE_ERROR_MESSAGE = "The new size is not in correct format, try again"
ROTATE_IMAGE_MESSAGE = "Choose the direction of the rotation R/L: "
DIRECTION_ERROR_MESSAGE = "You can enter only 'R' or 'L', try again: "
ENTER_EDGES_MESSAGE = "Enter kernel size, block size and 'c' e.g: '5,3,1.13':  "
EDGES_ERROR_MESSAGE = "your input is in the wrong format, try again: "
QUANTIZE_IMAGE_MESSAGE = "Enter the number shades you like (1-255): "
QUANTIZE_ERROR_MESSAGE = "The number you typed is not in the right format, try again: "
OPTIONS_ERROR_MESSAGE = "Your choice is not exist or not in the right format, try again: "
ENTER_DIRECTORY_MESSAGE = "Enter directory for saving the new image: "

def separate_channels(image: ColoredImage) -> List[SingleChannelImage]:

    all_channels = []
    for c in range(len(image[0][0])):
        all_channels.append([])
        for i in range(len(image)):
            all_channels[c].append([])
            for j in range(len(image[0])):
                all_channels[c][i].append(image[i][j][c])
    return all_channels


def combine_channels(channels: List[SingleChannelImage]) -> ColoredImage:
    combined_image = []
    for i in range(len(channels[0])):
        combined_image.append([])
        for j in range(len(channels[0][0])):
            combined_image[i].append([channels[c][i][j] for c in range(len(channels))])
    return combined_image


def RGB2grayscale(colored_image: ColoredImage) -> SingleChannelImage:
    gray_scale_image = []
    for i in range(len(colored_image)):
        gray_scale_image.append([])
        for j in range(len(colored_image[0])):
            index = round((0.299 * colored_image[i][j][0]) + (0.587 * colored_image[i][j][1]) + (0.114 * colored_image[i][j][2]))
            gray_scale_image[i].append(index)
    return gray_scale_image


def blur_kernel(size: int) -> Kernel:
    new_kernel = []
    value = 1/(size**2)
    for i in range(size):
        new_kernel.append([])
        for j in range(size):
            new_kernel[i].append(value)
    return new_kernel


def pixels_in_block(image: List ,block_size: int, coordinate):
    pixels = []
    image_rows = len(image)
    image_columns = len(image[0])
    block_radius = block_size // 2
    row_index = -1
    a = None  # coordinate_rows
    b = None  # coordinate_columns
    for i in range(-block_radius, block_radius+1):
        row_index += 1  # counts the index of the row for the pixels.
        if (coordinate[0] + i >= image_rows) or (coordinate[0] + i < 0):
            pixels.append([])
            for k in range(-block_radius, block_radius+1): pixels[row_index].append(None)
            continue
        else:
            a = coordinate[0] + i
            pixels.append([])
        for j in range(-block_radius, block_radius+1):
            if (coordinate[1] + j >= image_columns) or (coordinate[1] + j < 0):
                pixels[row_index].append(None)
                continue
            else:
                b = coordinate[1] + j
            pixels[row_index].append((a ,b))
    return pixels


def kernel_cell_value(image: List, kernel: list[int], coordinate):
    kernel_size = len(kernel)
    image_piece = pixels_in_block(image, kernel_size, coordinate)
    value = 0.0
    coordinate_value = image[coordinate[0]][coordinate[1]]
    for i in range(kernel_size):
        for j in range(kernel_size):
            if image_piece[i][j] is None:
                value += (coordinate_value * kernel[i][j])
            else:
                value += (image[image_piece[i][j][0]][image_piece[i][j][1]] * kernel[i][j])
    if value > 255:
        value = 255
    if value < 0:
        value = 0
    return round(value)


def apply_kernel(image: SingleChannelImage, kernel: Kernel) -> SingleChannelImage:
    new_image = []
    num_rows = len(image)
    num_columns = len(image[0])
    for i in range(num_rows):
        new_image.append([])
        for j in range(num_columns):
            new_image[i].append(kernel_cell_value(image, kernel, (i, j)))
    return new_image


def apply_kernel_colored_image(image: SingleChannelImage, kernel: Kernel):
    channels = separate_channels(image)
    for c in range(len(channels)):
        channels[c] = apply_kernel(channels[c], kernel)
    return combine_channels(channels)


def coordinates_location(source_image: List, coordinate: tuple, destination_image_size: tuple):
    new_y = (coordinate[0] / ((destination_image_size[0])-1)) * len(source_image)
    new_x = (coordinate[1] / ((destination_image_size[1])-1)) * len(source_image[0])
    if new_y >= len(source_image)-1:
        new_y = len(source_image)-1
    if new_x >= len(source_image[0])-1:
        new_x = len(source_image[0])-1
    return new_y, new_x


def bilinear_interpolation(image: SingleChannelImage, y: float, x: float) -> int:
    ratio_y = y % 1
    ratio_x = x % 1
    value_a = image[math.floor(y)][math.floor(x)]
    value_b = image[math.floor(y)][math.ceil(x)]
    value_c = image[math.ceil(y)][math.floor(x)]
    value_d = image[math.ceil(y)][math.ceil(x)]
    new_value = value_a*(1-ratio_y)*(1-ratio_x) + value_b*(1-ratio_y)*ratio_x + value_c*ratio_y*(1-ratio_x) + value_d*ratio_y*ratio_x
    return round(new_value)


def resize(image: SingleChannelImage, new_height: int, new_width: int) -> SingleChannelImage:
    new_image = []
    for i in range(new_height):
        new_image.append([])
        for j in range(new_width):
            a ,b = coordinates_location(image, (i ,j), (new_height, new_width))
            new_image[i].append(bilinear_interpolation(image, a, b))
    return new_image


def resize_colored_image(image: SingleChannelImage, new_height: int, new_width: int):
    channels = separate_channels(image)
    for c in range(len(channels)):
        channels[c] = resize(channels[c], new_height, new_width)
    return combine_channels(channels)


def rotate_90(image: Image, direction: str) -> Image:
     new_image = []
     if direction == 'L':
         for i in range(len(image)):
             for j in range(len(image[0])):
                 if i == 0:
                     new_image.append([image[i][j]])
                 else:
                     new_image[j].append(image[i][j])
         return new_image

     elif direction == 'R':
         for i in range(len(image)-1, -1, -1):
             for j in range(len(image[0])):
                 if i == len(image)-1:
                     new_image.append([image[i][j]])
                 else:
                     new_image[j].append(image[i][j])
         return new_image

     else:
         return None


def rotate_90_colored_image(image: Image, direction: str):
    channels = separate_channels(image)
    for c in range(len(channels)):
        channels[c] = rotate_90(channels[c], direction)
    return combine_channels(channels)


def get_average_by_block(image, coordinate, block):
    average = 0
    coordinate_value = image[coordinate[0]][coordinate[1]]
    for i in range(len(block)):
        for j in range(len(block)):
            if block[i][j] is None:
                average += coordinate_value
            else:
                average += image[block[i][j][0]][block[i][j][1]]
    average = average // (len(block)**2)
    return average


def is_threshold(blured_image, coordinate, block_size, c):
    a = coordinate[0]
    b = coordinate[1]
    block = pixels_in_block(blured_image, block_size, coordinate)
    average = get_average_by_block(blured_image, coordinate, block)
    if average - c > blured_image[a][b]:
        return False
    else:
        return True


def get_edges(image: SingleChannelImage, blur_size: int, block_size: int, c: float) -> SingleChannelImage:
    kernel = blur_kernel(blur_size)
    blured_image = apply_kernel(image, kernel)
    new_image = []
    for i in range(len(image)):
        new_image.append([])
        for j in range(len(image[0])):
            if is_threshold(blured_image, (i, j), block_size, c):
                new_image[i].append(255)
            else:
                new_image[i].append(0)
    return new_image


def get_edges_colored_image(image: SingleChannelImage, blur_size: int, block_size: int, c: float):
    channels = separate_channels(image)
    for ch in range(len(channels)):
        channels[ch] = get_edges(channels[ch], blur_size, block_size, c)
    return combine_channels(channels)


def quantize(image: SingleChannelImage, N: int) -> SingleChannelImage:
    new_image = []
    for i in range(len(image)):
        new_image.append([])
        for j in range(len(image[0])):
            quant = round(math.floor(image[i][j] * (N/256)) * (255 / (N-1)))
            new_image[i].append(quant)
    return new_image


def quantize_colored_image(image: ColoredImage, N: int) -> ColoredImage:
    channels = separate_channels(image)
    for c in range(len(channels)):
        channels[c] = quantize(channels[c], N)
    return combine_channels(channels)


def user_grayscale_image(image: List):
    if is_one_channel(image):
        print("The image is already grayscale")
    else:
        return RGB2grayscale(image)


def user_blur_image(image: List):
    kernel = input(ENTER_KERNEL_MESSAGE)
    while True:
        if is_valid_kernel(kernel):
            break
        kernel = input(KERNEL_ERROR_MESSAGE)
    kernel = blur_kernel(int(kernel))
    if is_one_channel(image):
        return apply_kernel(image, kernel)
    else:
        return apply_kernel_colored_image(image, kernel)


def is_int(s):
    '''Checks if a string can be casted to an integer'''
    try:
        int(s)
        return True
    except ValueError:
        return False


def is_float(s):
    '''Checks if a string can be casted to an integer'''
    try:
        float(s)
        return True
    except ValueError:
        return False


def user_resize(image: List):
    new_size = input(ENTER_SIZE_OF_IMAGE_MESSAGE).split(',')
    while True:
        if len(new_size) == 2:
            if is_int(new_size[0]) and is_int(new_size[1]):
                break
        new_size = input(NEW_SIZE_ERROR_MESSAGE).split(',')
    if is_one_channel(image):
        return resize(image, int(new_size[0]), int(new_size[1]))
    else:
        return resize_colored_image(image, int(new_size[0]), int(new_size[1]))


def user_rotate_image(image: List):
    direction = input(ROTATE_IMAGE_MESSAGE)
    while True:
        if direction == 'R' or direction == 'L':
            break
        direction = input(DIRECTION_ERROR_MESSAGE)
    return rotate_90(image, direction)


def is_valid_kernel(kernel):
    if is_int(kernel):
        if int(kernel) % 2 == 1 and int(kernel) > 2:
            return True
    else:
        return False


def user_get_edges(image: List):
    user_input = input(ENTER_EDGES_MESSAGE).split(',')
    while True:
        if len(user_input) == 3:
            blur_size = user_input[0]
            block_size = user_input[1]
            c = user_input[2]
            if is_valid_kernel(blur_size) and is_valid_kernel(block_size):
                if is_float(c):
                    c = float(c)
                    if c >= 0:
                        blur_size = int(blur_size)
                        block_size = int(block_size)
                        break
        user_input = input(EDGES_ERROR_MESSAGE)
    if is_one_channel(image):
        return get_edges(image, blur_size, block_size, c)
    else:
        return get_edges_colored_image(image, blur_size, block_size, c)


def is_one_channel(image: List):
    if type(image[0][0]) is int:
        return True
    return False


def user_quantize_image(image: List):
    shades = input(QUANTIZE_IMAGE_MESSAGE)
    while True:
        if is_int(shades):
            shades = int(shades)
            if 1 < shades < 256:
                break
        shades = input(QUANTIZE_ERROR_MESSAGE)
    if is_one_channel(image):
        return quantize(image, shades)
    else:
        return quantize_colored_image(image, shades)


if __name__ == '__main__':
    source_image = load_image(sys.argv[1], RGB_CODE)
    edit_image = source_image
    user_input = input(OPTIONS_MESSAGE)
    while True:
        if not is_int(user_input):
            user_input = input(OPTIONS_ERROR_MESSAGE)
            continue
        elif int(user_input) < 1 or int(user_input) > 8:
            user_input = input(OPTIONS_ERROR_MESSAGE)
            continue
        else:
            if user_input == '1':
                check = user_grayscale_image(edit_image)
                if check is not None:
                    edit_image = check
            elif user_input == '2':
                edit_image = user_blur_image(edit_image)
            elif user_input == '3':
                edit_image = user_resize(edit_image)
            elif user_input == '4':
                edit_image = user_rotate_image(edit_image)
            elif user_input == '5':
                edit_image = user_get_edges(edit_image)
            elif user_input == '6':
                edit_image = user_quantize_image(edit_image)
            elif user_input == '7':
                show_image(edit_image)
            elif user_input == '8':
                break
            user_input = input(OPTIONS_MESSAGE)
    directory = input(ENTER_DIRECTORY_MESSAGE)
    save_image(edit_image, directory)
