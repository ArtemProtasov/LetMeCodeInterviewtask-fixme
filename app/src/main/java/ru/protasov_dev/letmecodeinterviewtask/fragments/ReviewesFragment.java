package ru.protasov_dev.letmecodeinterviewtask.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.protasov_dev.letmecodeinterviewtask.App;
import ru.protasov_dev.letmecodeinterviewtask.R;
import ru.protasov_dev.letmecodeinterviewtask.Utils;
import ru.protasov_dev.letmecodeinterviewtask.activity.ReviewPage;
import ru.protasov_dev.letmecodeinterviewtask.adapters.ReviewesListAdapter;
import ru.protasov_dev.letmecodeinterviewtask.endlessrecyclereiew.EndlessRecyclerViewReviews;
import ru.protasov_dev.letmecodeinterviewtask.parsetaskmanagers.PostModelReviews.PostModelReviews;
import ru.protasov_dev.letmecodeinterviewtask.parsetaskmanagers.PostModelReviews.Result;

import static android.R.color.holo_blue_bright;
import static android.R.color.holo_green_light;
import static android.R.color.holo_orange_light;
import static android.R.color.holo_red_light;

public class ReviewesFragment extends Fragment implements
        EndlessRecyclerViewReviews.OnLoadMoreListener,
        ReviewesListAdapter.ReviewesListener {

    private int currentPage = 0;

    private EditText etKeywords;
    private ReviewesListAdapter reviewesListAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View view;
    private final int PERMISSION_REQUEST_CODE = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.reviewes_fragment, container, false);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        this.view = view;
        swipeRefreshLayout = view.findViewById(R.id.swipe_container);
        etKeywords = view.findViewById(R.id.keyword);

        EndlessRecyclerViewReviews recyclerView = view.findViewById(R.id.recycler_reviews);
        reviewesListAdapter = new ReviewesListAdapter(this);
        recyclerView.setAdapter(reviewesListAdapter);
        recyclerView.setOnLoadMoreListener(this);

        swipeRefreshLayout.setColorSchemeResources(holo_blue_bright,
                holo_green_light,
                holo_orange_light,
                holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(null, null, true, 0);
            }
        });

        etKeywords.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    getData(null, null, true, 0);
                    return true;
                }
                return false;
            }
        });

        ImageButton imageButtonClearKeyword = view.findViewById(R.id.clear_keywords);
        imageButtonClearKeyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etKeywords.setText(null);
                getData(null, null, true, 0);
            }
        });

        getData(null, null, false, currentPage);
    }

    @Override
    public void onLoadMore() {
        currentPage += 20;
        getData(null, null, false, currentPage);
    }

    private void getData(String reviewer, String order, boolean clear, int offset) {
        if (clear) {
            reviewesListAdapter.clearItems();
        }

        App.getApi().getAllReviews(getString(R.string.api_key_nyt), etKeywords.getText().toString(),
                reviewer, offset, order).enqueue(new Callback<PostModelReviews>() {
            @Override
            public void onResponse(Call<PostModelReviews> call, Response<PostModelReviews> response) {
                if (response.body() != null) {
                    reviewesListAdapter.addItems(response.body().getResults());
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<PostModelReviews> call, Throwable t) {
                t.printStackTrace();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onReviewesItemClick(Result item) {
        Intent startReviewPage = new Intent(getContext(), ReviewPage.class)
                .putExtra("URL", item.getLink().getUrl())
                .putExtra("ARTICLE_TITLE", item.getDisplayTitle());
        startActivity(startReviewPage);
    }

    private Bitmap bitmap;
    private String title;

    @Override
    public void onReviewesLongImageClick(final ImageView imageView, final String title) {

        this.title = title;
        this.bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.save_image)
                .setMessage(R.string.save_or_cancel)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!savePictures()) {
                            requestPermission();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Snackbar.make(getView(), R.string.image_not_saved, Snackbar.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    private boolean savePictures() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            try {
                Utils.savePicture(getContext(), title, bitmap);
                Snackbar.make(view, getString(R.string.image_saved), Snackbar.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Snackbar.make(view, getString(R.string.image_not_saved), Snackbar.LENGTH_SHORT).show();
            }
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.no_permission)
                .setMessage(R.string.allow_save_pictures)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requestPermissions(new String[]{
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                                },
                                0);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Snackbar.make(getView(), R.string.image_not_saved, Snackbar.LENGTH_SHORT)
                                .show();
                    }
                })
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        Utils.savePicture(getContext(), title, bitmap);
                        Snackbar.make(view, getString(R.string.image_saved), Snackbar.LENGTH_SHORT)
                                .show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Snackbar.make(view, getString(R.string.image_not_saved), Snackbar.LENGTH_SHORT)
                                .show();
                    }
                } else {
                    Snackbar.make(getView(),
                            R.string.no_permission,
                            Snackbar.LENGTH_SHORT).show();
                }
                return;
            }
        }
        Snackbar.make(getView(), R.string.no_permission, Snackbar.LENGTH_SHORT)
                .show();
    }
}
