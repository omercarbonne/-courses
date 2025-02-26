//
// Created on 2/20/2022.
//

#ifndef RECOMMENDATIONSYSTEM_H
#define RECOMMENDATIONSYSTEM_H
#include "User.h"
#include<map>
#include <cmath>
#include <set>


struct compare_func{
    bool operator()(const sp_movie &m1, const sp_movie &m2) const
    {
        return *m1 < *m2;
    }
};

struct set_compare{
    bool operator()(const std::pair<sp_movie, double> p1,
            const std::pair<sp_movie, double> p2)
    {
        return p1.second < p2.second;
    }
};
typedef std::map<sp_movie, std::vector<double>, compare_func> movies_map;
class RecommendationSystem
{
private:
    movies_map _movies_map;

public:

	//explicit RecommendationSystem()
    RecommendationSystem();
    /**
     * adds a new movie to the system
     * @param name name of movie
     * @param year year it was made
     * @param features features for movie
     * @return shared pointer for movie in system
     */
	sp_movie add_movie(const std::string& name,int year,
                       const std::vector<double>& features);


    /**
     * a function that calculates the movie with highest
     * score based on movie features
     * @param ranks user ranking to use for algorithm
     * @return shared pointer to movie in system
     */
	sp_movie recommend_by_content(const User& user);

    /**
     * a function that calculates the movie with highest predicted
     * score based on ranking of other movies
     * @param ranks user ranking to use for algorithm
     * @param k
     * @return shared pointer to movie in system
     */
	sp_movie recommend_by_cf(const User& user, int k);


    /**
     * Predict a user rating for a movie given argument using item
     * cf procedure with k most similar movies.
     * @param user_rankings: ranking to use
     * @param movie: movie to predict
     * @param k:
     * @return score based on algorithm as described in pdf
     */
	double predict_movie_score(const User &user, const sp_movie &movie,
												  int k);

	/**
	 * gets a shared pointer to movie in system
	 * @param name name of movie
	 * @param year year movie was made
	 * @return shared pointer to movie in system
	 */
	sp_movie get_movie(const std::string &name, int year) const;


    /**
     * output stream operator
     * @param os the output stream
     * @param rec_sys the recommendation system
     * @return output stream
     */
     friend std::ostream& operator<<(std::ostream &os,
             RecommendationSystem &rec_sys);
private:
    double get_average(const User &user) const;
    rank_map& normalize(rank_map &ranks, double average) const;
    std::vector<double> get_pref_vector(const User &user);
    double get_angel(const std::vector<double> &vec1,
                     const std::vector<double> &vec2);
    double mult(const std::vector<double> &vec1,
                const std::vector<double> &vec2) const;
    std::vector<double> add(const std::vector<double> &vec1,
                            const std::vector<double> &vec2) const;
};


#endif //RECOMMENDATIONSYSTEM_H
