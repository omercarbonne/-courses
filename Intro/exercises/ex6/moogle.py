import pickle
import urllib.parse
import requests
import bs4
import sys


def get_lines_from_file(file_directory: str): # -> ListOfHrefsInsideFile:
    """
    :param file_directory: the file we want to read
    :return: a list of all the lines in the file, without '/n' at the end
    """
    with open(file_directory, 'r') as file:
        hrefs_list = [href.strip() for href in file]
    return hrefs_list


def get_hrefs_from_html(html: str):
    """
    :param html: to the wanted webpage
    :return: a list of all the hrefs in the html
    """
    new_list = []
    soup = bs4.BeautifulSoup(html,'html.parser')
    for p in soup.find_all("p"):
        for link in p.find_all("a"):
            target = link.get("href")
            if target != "":
                new_list.append(target)
    return new_list


def get_hrefs_dict(url_path: str, home_hrefs: list):
    """
    :param url_path for the web page.
    :return: a dict with all the hrefs in it: key - hrefs, value - num of hrefs
    """
    response = requests.get(url_path)
    html = response.text
    hrefs_list = get_hrefs_from_html(html)
    new_dict = dict()
    for href in hrefs_list:
        if href == "" or href not in home_hrefs:
            continue
        if href in new_dict:
            new_dict[href] += 1
        else:
            new_dict[href] = 1
    return new_dict


def crawl(base_url: str, index_file: str, new_file_name: str):
    """
    :param index_file: file which contains a list of hrefs separated with '/n'.
    :param base_url: the base url of all the hrefs.
    :param new_file_name: the name of the new pickle file which will contain the dict
    :return: a dict the contains for each url a dict with all the hrefs in it and the amount of them.
    """
    hrefs_list = get_lines_from_file(index_file)
    new_dict = dict()
    for href in hrefs_list:
        full_url = urllib.parse.urljoin(base_url, href)
        new_dict[href] = get_hrefs_dict(full_url, hrefs_list)
    save_in_pickle_file(new_dict, new_file_name)


def save_in_pickle_file(dict: dict, new_file_name: str):
    """
    saves the given text in pickle file.
    :return: None
    """
    with open(new_file_name, 'wb') as f:
        pickle.dump(dict, f)


def open_pickle_file(file_name: str):
    """
    opens pickle file
    :return: dict
    """
    with open(file_name, 'rb') as f:
        new_dict = pickle.load(f)
    return new_dict


def init_rank_dict(hrefs_dict: dict[str: dict[str: int]], rank: float):
    """creates a new rank_dict which all the pages in it have the given rank"""
    new_rank_dict = dict()
    for href in hrefs_dict:
        new_rank_dict[href] = rank
    return new_rank_dict


def calculate_sum_of_all_hrefs(hrefs_dict: dict[str:int]):
    """Calculates the total num of hrefs in a given hrefs_dict
    :return: float: the total num of hrefs"""
    sum = 0
    for value in hrefs_dict.values():
        sum += value
    return sum


def calculate_new_rank(current_rank: float, href: str, rank_dict: dict[str:float], hrefs_dict: dict[str: dict[str: int]]):
    """
    calculates the new rank of a given href according to a given rank_dict and hrefs_dict.
    if it's the first iteration the ranks will be -1.
    :return: new rank (float)
    """
    if current_rank != -1: # if it's not the first iteration
        new_rank = 0
        for k, h in hrefs_dict.items():
            if href in h:
                total_hrefs = calculate_sum_of_all_hrefs(h)
                a = rank_dict[k]
                b = (h[href]/total_hrefs)
                new_rank += (h[href]/total_hrefs) * rank_dict[k]
        return new_rank
    else:  # if it's the first iteration
        new_rank = 0
        for h in hrefs_dict.values():
            if href in h:
                total_hrefs = calculate_sum_of_all_hrefs(h)
                new_rank += h[href] / total_hrefs
        return new_rank


def do_iteration(rank_dict: dict[str:float], hrefs_dict: dict[str: dict[str: int]]):
    """
    the function does one iteration to rank_dict according to the values in hrefs_dict.
    :return: updated rank_dict
    """
    new_rank_dict = dict()
    for page, rank in rank_dict.items():
        new_rank_dict[page] = calculate_new_rank(rank, page, rank_dict, hrefs_dict)
    return new_rank_dict


def page_rank(iterations: int, hrefs_dict_file: str, out_file_name: str):
    """
   the function creates a rank_dict according to the hrefs_dict and the number of iterations,
   and save it in pickle file with the name given in out_file_name.
    """
    hrefs_dict = open_pickle_file(hrefs_dict_file)
    iterations = int(iterations)
    if iterations == 0:
        rank_dict = init_rank_dict(hrefs_dict, 1)
    else:
        rank_dict = init_rank_dict(hrefs_dict, -1)
        for i in range(iterations):
            rank_dict = do_iteration(rank_dict, hrefs_dict)
    save_in_pickle_file(rank_dict, out_file_name)
    return rank_dict


def update_words_dict(base_url: str, href: str, words_dict: dict[str:dict[str:int]]):
    """
    creates a dict which contain all the words in given web_page, and thw amount of each one of them.
    :param url_path:
    :return: dict[str, int]
    """
    full_url = urllib.parse.urljoin(base_url, href)
    response = requests.get(full_url)
    html = response.text
    soup = bs4.BeautifulSoup(html, 'html.parser')
    for p in soup.find_all("p"):
        content = p.text.split()
        for word in content:
            if word == '':
                continue
            if word in words_dict:
                if href in words_dict[word]:
                    words_dict[word][href] += 1
                else:
                    words_dict[word][href] = 1
            else:
                words_dict[word] = {href: 1}


def words_dict(base_url: str, index_file: str, new_file_name: str):
    hrefs_list = get_lines_from_file(index_file)
    new_dict = dict()
    for href in hrefs_list:
        update_words_dict(base_url, href, new_dict)
    save_in_pickle_file(new_dict, new_file_name)


def get_words_rank(query: str, words_dict: dict[str:dict[str:int]], href: str):
    """
    calculates the words rank according to given query and given words dict (one href).
    """
    words = query.split(" ")
    words_rank = []
    for word in words:
        if word in words_dict:
            if href in words_dict[word]:
                words_rank.append(words_dict[word][href])
            else:
                words_rank.append(0)
    if words_rank == []:
        return 0
    else:
        return min(words_rank)



def get_page_grade(query: str, href: str, rank_dict: dict[str:int], words_dict: dict[str:dict[str, int]]):
    """
    calculates the rank of a web page according to given query, words_dict, and rank_dict.
    :return: the rank of the web page
    """
    if href not in rank_dict:
        None
    else:
        page_rank = rank_dict[href]
        words_rank = get_words_rank(query, words_dict, href)
        if words_rank == 0:
            return None
    return page_rank, words_rank


def get_search_results(query: str, rank_dict: dict[str,int], words_dict: dict[str:dict[str:int]], max_result: int):
    """
    calculates the grades for each web page according to given query,
    and returns the top results according to the max argument.
    :return: list of tuples (href: str, grade: float) sorted highest to lowest.
    """
    results = []
    for href in rank_dict:
        grade = get_page_grade(query, href, rank_dict, words_dict)
        if grade is not None:
            results.append((href, grade))
    results.sort(key=lambda a: a[1][0], reverse=True)  # sort by hrefs rank
    if len(results) > max_result:
        results = results[:max_result]
    for i in range(len(results)):
        results[i] = (results[i][0], results[i][1][0]*results[i][1][1])  # apply the words rank
    results.sort(key=lambda a: a[1], reverse=True)  # final sort
    return results


def search(query: str, rank_dict_file: str, words_dict_file: str, max_result: int):
    rank_dict = open_pickle_file(rank_dict_file)
    words_dict = open_pickle_file(words_dict_file)
    max_result = int(max_result)
    results = get_search_results(query, rank_dict, words_dict, max_result)
    for r in results:
        print(r[0], r[1])


if __name__ == "__main__":
    command = sys.argv[1]
    if command == "crawl":
        crawl(sys.argv[2], sys.argv[3], sys.argv[4])
    if command == "page_rank":
        page_rank(sys.argv[2], sys.argv[3], sys.argv[4])
    if command == "words_dict":
        words_dict(sys.argv[2], sys.argv[3], sys.argv[4])
    if command == "search":
        search(sys.argv[2], sys.argv[3], sys.argv[4], sys.argv[5])


