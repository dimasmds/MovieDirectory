package com.riotfallen.moviedirectory.core.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.riotfallen.moviedirectory.core.BuildConfig;
import com.riotfallen.moviedirectory.core.R;
import com.riotfallen.moviedirectory.core.api.APIRepository;
import com.riotfallen.moviedirectory.core.api.Client;
import com.riotfallen.moviedirectory.core.model.video.VideoResponse;
import com.riotfallen.moviedirectory.core.view.VideoView;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoPresenter {

    private VideoView videoView;
    private Context context;

    public VideoPresenter(VideoView videoView, Context context) {
        this.videoView = videoView;
        this.context = context;
    }

    public void getVideo(int movieId){
        final APIRepository repository = Client.getClient().create(APIRepository.class);
        Call<VideoResponse> call = repository.getVideos(movieId, BuildConfig.API_KEY);
        call.enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(@NonNull Call<VideoResponse> call, @NonNull Response<VideoResponse> response) {
                if(response.isSuccessful()){
                    if(Objects.requireNonNull(response.body()).getResults().size() > 0){
                        videoView.showVideoData(response.body().getResults().get(0));
                    } else {
                        videoView.showVideoNotFound(context.getString(R.string.message_video_not_found));
                    }
                } else {
                    videoView.showVideoError(response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<VideoResponse> call, @NonNull Throwable t) {
                videoView.showVideoError(t.getMessage());
            }
        });
    }
}
