package com.example.perfectfitapp_android.model.ModelServer;

import android.util.Log;

import com.example.perfectfitapp_android.MyApplication;
import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Post;
import com.example.perfectfitapp_android.model.Profile;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostModelServer {

    Server server = new Server();

    public void getAllPosts(Model.GetAllPostsListener listener) {
        String token = server.sp.getString("ACCESS_TOKEN", "");
        Call<JsonArray> call = server.service.executeGetAllPosts(token);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                if (response.isSuccessful()) {
                    JsonArray postsJson = response.body();
                    List<Post> posts = Post.jsonArrayToPost(postsJson);
                    listener.onComplete(posts);
                } else if (response.code() == 400) {
                    Log.d("TAG", "failed in getAllPosts in ModelServer 1");
                    listener.onComplete(null);
                } else if (response.code() == 403) {
                    Model.instance.refreshToken(tokensList -> {
                        if (tokensList != null) {
                            Model.instance.insertTokens(tokensList);
                            System.out.println("********************************* change the token *********************************");
                            listener.onComplete(null);
                        } else {
                            listener.onComplete(null);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("TAG", "failed in getAllPosts in ModelServer 2");
                listener.onComplete(null);
            }
        });
    }

    public void addNewPost(Post post, Model.AddNewPostListener listener) {
        HashMap<String, Object> postMap = post.toJson();
        String token = server.sp.getString("ACCESS_TOKEN", "");
        Call<JsonObject> call = server.service.addNewPost(token, postMap);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200) {
                    Post newPost = new Post();
                    newPost = Post.jsonElementToPost(response.body().get("post"));
                    listener.onComplete(newPost);
                } else if (response.code() == 400) {
                    listener.onComplete(null);
                } else if (response.code() == 403) {
                    Model.instance.refreshToken(tokensList -> {
                        if (tokensList != null) {
                            Model.instance.insertTokens(tokensList);
                            System.out.println("********************************* change the token *********************************");
                            addNewPost(post, listener);
                        } else {
                            listener.onComplete(null);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("TAG", "problem in createProfile in ModelServer 2");
                listener.onComplete(null);
            }
        });
    }


    public void deletePost(String postId, Model.deletePostFromServerListener listener) {
        String token = server.sp.getString("ACCESS_TOKEN", "");
        Call<Void> call = server.service.deletePost(token, postId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    listener.onComplete(true);
                } else if (response.code() == 400) {
                    Log.d("TAG", "failed in ModelServer in deletePost 1");
                    listener.onComplete(false);
                } else if (response.code() == 403) {
                    Model.instance.refreshToken(tokensList -> {
                        if (tokensList != null) {
                            Model.instance.insertTokens(tokensList);
                            System.out.println("********************************* change the token *********************************");
                            deletePost(postId, listener);
                        } else {
                            listener.onComplete(false);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("TAG", "failed in ModelServer in deletePost 2");
                listener.onComplete(false);
            }
        });
    }

    public void editPost(Post post, Model.editPostListener listener) {
        String token = server.sp.getString("ACCESS_TOKEN", "");
        HashMap<String, Object> postMap = post.toJson();

        Call<Void> call = server.service.editPost(token, postMap);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    listener.onComplete(true);
                } else if (response.code() == 400) {
                    Log.d("TAG", "failed in ModelServer in editPost 1");
                    listener.onComplete(false);
                } else if (response.code() == 403) {
                    Model.instance.refreshToken(tokensList -> {
                        if (tokensList != null) {
                            Model.instance.insertTokens(tokensList);
                            System.out.println("********************************* change the token *********************************");
                            editPost(post, listener);
                        } else {
                            listener.onComplete(false);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("TAG", "failed in ModelServer in editPost 2");
                listener.onComplete(false);
            }
        });
    }


    public void getProfilePosts(String userName, Model.getProfilePostsListener listener) {
        String token = server.sp.getString("ACCESS_TOKEN", "");
        Call<JsonArray> call = server.service.getProfilePosts(token, userName);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.code() == 200) {
                    List<Post> posts = Post.jsonArrayToPost(response.body());
                    listener.onComplete(posts);
                } else if (response.code() == 400) {
                    Log.d("TAG", "failed in ModelServer in getProfilePosts 1");
                    listener.onComplete(null);
                } else if (response.code() == 403) {
                    Model.instance.refreshToken(tokensList -> {
                        if (tokensList != null) {
                            Model.instance.insertTokens(tokensList);
                            System.out.println("********************************* change the token *********************************");
                            getProfilePosts(userName, listener);
                        } else {
                            listener.onComplete(null);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("TAG", "failed in ModelServer in getProfilePosts 2");
                listener.onComplete(null);
            }
        });
    }


    public void getPostById(String postId, Model.getPostByIdListener listener) {
        String token = server.sp.getString("ACCESS_TOKEN", "");
        Call<JsonElement> call = server.service.getPostById(token, postId);

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.code() == 200) {
                    JsonElement js = response.body();
                    Post post = Post.jsonElementToPost(js);
                    listener.onComplete(post);
                } else if (response.code() == 403) {
                    System.out.println("403 in posts model server - getPostById - 403");
                    Model.instance.refreshToken(tokensList -> {
                        if (tokensList != null) {
                            Model.instance.insertTokens(tokensList);
                            System.out.println("********************************* change the token *********************************");
                            getPostById(postId, listener);
                        } else {
                            listener.onComplete(null);
                        }
                    });
                } else {
                    Log.d("TAG", "failed in ModelServer in getProfilePosts 2");
                    listener.onComplete(null);
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("TAG", "failed in ModelServer in getProfilePosts 2");
                listener.onComplete(null);
            }
        });

    }

    public void getPostsBySubCategoryId(String subCategoryId, Model.GetPostsBySubCategoryIdListener listener) {
        String token = server.sp.getString("ACCESS_TOKEN", "");
        Call<JsonArray> call = server.service.getPostsBySubCategoryId(token, subCategoryId);

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.code() == 200) {
                    JsonArray postsJson = response.body();
                    List<Post> posts = Post.jsonArrayToPost(postsJson);
                    listener.onComplete(posts);
                } else if (response.code() == 400) {
                    Log.d("TAG", "failed in ModelServer in getProfilePosts 2");
                    listener.onComplete(null);
                } else if (response.code() == 403) {
                    Model.instance.refreshToken(tokensList -> {
                        if (tokensList != null) {
                            Model.instance.insertTokens(tokensList);
                            System.out.println("********************************* change the token *********************************");
                            getPostsBySubCategoryId(subCategoryId, listener);
                        } else {
                            listener.onComplete(null);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("TAG", "failed in ModelServer in getProfilePosts 2");
                listener.onComplete(null);
            }
        });

    }

    public void getWishList(Model.getWishListListener listener) {
        String token = server.sp.getString("ACCESS_TOKEN", "");
        Call<JsonArray> call = server.service.getWishList(token, Model.instance.getProfile().getUserName());

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.code() == 200) {
                    JsonArray wishListArray = response.body();
                    List<Post> postsWishList = new ArrayList<>();
                    postsWishList = Post.jsonArrayToPost(response.body());
                    listener.onComplete(postsWishList);
                } else if (response.code() == 400) {
                    Log.d("TAG", "failed in getWishList in ModelServer 1");
                    listener.onComplete(null);
                } else if (response.code() == 403) {
                    Model.instance.refreshToken(tokensList -> {
                        if (tokensList != null) {
                            Model.instance.insertTokens(tokensList);
                            System.out.println("********************************* change the token *********************************");
                            getWishList(listener);
                        } else {
                            listener.onComplete(null);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("TAG", "failed in getWishList in ModelServer 2");
                listener.onComplete(null);
            }
        });

    }

    public void getDatesFromServer(String date, Model.getDatesListener listener) {
        String token = server.sp.getString("ACCESS_TOKEN", "");

        Call<JsonArray> call = server.service.getDates(token, date);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.code() == 200) {
                } else if (response.code() == 403) {
                    Model.instance.refreshToken(tokensList -> {
                        if (tokensList != null) {
                            Model.instance.insertTokens(tokensList);
                            System.out.println("********************************* change the token *********************************");
                            getDatesFromServer(date, listener);
                        } else {
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
            }
        });
    }

    public void timeSince(String date, Model.TimeSinceListener listener) {
        String token = server.sp.getString("ACCESS_TOKEN", "");

        Call<JsonElement> call = server.service.timeSince(token, date);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.code() == 200) {
                    String timeAgo = response.body().getAsJsonObject().get("timeAgo").getAsString() + " ago";
                    listener.onComplete(timeAgo);
                } else if (response.code() == 400) {
                    listener.onComplete(null);
                    Log.d("TAG", "failed in timeSince");
                } else if (response.code() == 403) {
                    Model.instance.refreshToken(tokensList -> {
                        if (tokensList != null) {
                            Model.instance.insertTokens(tokensList);
                            System.out.println("********************************* change the token *********************************");
                            timeSince(date, listener);
                        } else {
                            System.out.println(" -------------- problem in PostModelServer 378 ----------- ");
                            listener.onComplete(null);
                        }

                    });
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("TAG", t.getMessage());
                listener.onComplete(null);
            }
        });
    }


    public void getSuitablePosts(String profileId, Model.getSuitablePostsListener listener) {
        String token = server.sp.getString("ACCESS_TOKEN", "");

        Call<JsonArray> call = server.service.getSuitablePosts(token, profileId);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.code() == 200) {
                    JsonArray js = response.body();
                    List<Post> list = Post.jsonArrayToPost(js);
                    System.out.println("the list is: " + list);
                    listener.onComplete(list);
                } else if (response.code() == 400) {
                    listener.onComplete(null);
                    Log.d("TAG", "failed in getSuitablePosts 1");
                } else if (response.code() == 403) {
                    Model.instance.refreshToken(tokensList -> {
                        if (tokensList != null) {
                            Model.instance.insertTokens(tokensList);
                            System.out.println("********************************* change the token *********************************");
                            getSuitablePosts(profileId, listener);
                        } else {
                            listener.onComplete(null);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                listener.onComplete(null);
                Log.d("TAG", "failed in getSuitablePosts 2");
            }
        });
    }


    public void getSearchPosts(HashMap<String, List<String>> map, Model.getSearchPostsListener listener) {
        String token = server.sp.getString("ACCESS_TOKEN", "");
        Call<JsonArray> call = server.service.getSearchPosts(token, map);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.code() == 200) {
                    List<Post> list = Post.jsonArrayToPost(response.body());
                    listener.onComplete(list);
                } else if (response.code() == 400) {
                    listener.onComplete(null);
                    Log.d("TAG", "failed in getSearchPosts 1");
                } else if (response.code() == 403) {
                    Model.instance.refreshToken(tokensList -> {
                        if (tokensList != null) {
                            Model.instance.insertTokens(tokensList);
                            System.out.println("********************************* change the token *********************************");
                            getSearchPosts(map, listener);
                        } else {
                            listener.onComplete(null);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                listener.onComplete(null);
                Log.d("TAG", "failed in getSearchPosts 2");
            }
        });
    }


    public void getGeneral(Model.getGeneralListener listener) {

        // here we got the sizes/companies/gender etc. but not the categories
        // categories we get from getAllCategories.

        String token = server.sp.getString("ACCESS_TOKEN", "");
        Call<JsonObject> call = server.service.getGeneral(token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.code() == 200) {
                    JsonArray json = response.body().getAsJsonArray("gen");

                    JsonArray sizesJson = json.get(0).getAsJsonObject().get("sizes").getAsJsonArray();
                    ArrayList<String> sizes = jsonArrayToArrayList(sizesJson);
                    JsonArray companiesJson = json.get(0).getAsJsonObject().get("companies").getAsJsonArray();
                    ArrayList<String> companies = jsonArrayToArrayList(companiesJson);
                    JsonArray colorsJson = json.get(0).getAsJsonObject().get("colors").getAsJsonArray();
                    ArrayList<String> colors = jsonArrayToArrayList(colorsJson);
                    JsonArray bodyTypesJson = json.get(0).getAsJsonObject().get("bodyTypes").getAsJsonArray();
                    ArrayList<String> bodyTypes = jsonArrayToArrayList(bodyTypesJson);
                    JsonArray bodyTypeDescriptionJson = json.get(0).getAsJsonObject().get("bodyTypeDescription").getAsJsonArray();
                    ArrayList<String> bodyTypeDescription = jsonArrayToArrayList(bodyTypeDescriptionJson);
                    JsonArray genderJson = json.get(0).getAsJsonObject().get("gender").getAsJsonArray();
                    ArrayList<String> gender = jsonArrayToArrayList(genderJson);

                    HashMap<String, List<String>> map = new HashMap<>();
                    map.put("Sizes", sizes);
                    map.put("Companies", companies);
                    map.put("Colors", colors);
                    map.put("BodyTypes", bodyTypes);
                    map.put("BodyTypeDescription", bodyTypeDescription);
                    map.put("Gender", gender);

                    listener.onComplete(map);
                } else if (response.code() == 403) {
                    Model.instance.refreshToken(tokensList -> {
                        if (tokensList != null) {
                            Model.instance.insertTokens(tokensList);
                            System.out.println("********************************* change the token *********************************");
                            getGeneral(listener);
                        } else {
                            listener.onComplete(null);
                        }
                    });
                } else {
                    listener.onComplete(null);
                    Log.d("TAG", "failed in getGeneral 12 ");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                listener.onComplete(null);
                Log.d("TAG", "failed in getGeneral 2");
            }
        });
    }

    public ArrayList<String> jsonArrayToArrayList(JsonArray js) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < js.size(); i++) {
            list.add(js.get(i).getAsString());
        }
        return list;
    }


    public List<String> changeJsonListToRegularList(JsonElement js) {
        List<String> list = new ArrayList<>();
        JsonArray arr = js.getAsJsonArray();
        for (int i = 0; i < arr.size(); i++) {
            list.add(arr.get(i).getAsString());
        }
        return list;
    }

    public void getPostsByIds(List<String> postsId, Model.GetPostsByIdsListener listener) {
        String token = server.sp.getString("ACCESS_TOKEN", "");
        Call<JsonArray> call = server.service.getPostsByIds(token, postsId);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.code() == 200) {

                    List<Post> posts = Post.jsonArrayToPost(response.body());
                    listener.onComplete(posts);
                } else if (response.code() == 400) {
                    Log.d("TAG", "failed in getPostsByIds");
                    listener.onComplete(null);
                } else if (response.code() == 403) {
                    Model.instance.refreshToken(tokensList -> {
                        if (tokensList != null) {
                            Model.instance.insertTokens(tokensList);
                            System.out.println("********************************* change the token *********************************");
                            getPostsByIds(postsId, listener);
                        } else {
                            listener.onComplete(null);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("TAG", "failed in getPostsByIds");
                listener.onComplete(null);
            }
        });
    }
}
