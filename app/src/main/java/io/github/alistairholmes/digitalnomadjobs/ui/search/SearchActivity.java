package io.github.alistairholmes.digitalnomadjobs.ui.search;

import android.app.SearchManager;
import android.app.SharedElementCallback;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.TransitionRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import io.github.alistairholmes.digitalnomadjobs.R;
import io.github.alistairholmes.digitalnomadjobs.data.model.Job;
import io.github.alistairholmes.digitalnomadjobs.data.model.SearchResponse;
import io.github.alistairholmes.digitalnomadjobs.ui.adapter.SearchJobsAdapter;
import io.github.alistairholmes.digitalnomadjobs.utils.ImeUtils;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = SearchActivity.class.getName();
    public static final String EXTRA_QUERY = "EXTRA_QUERY";
    public static final int RESULT_CODE_SAVE = 7;

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private SearchViewModel searchViewModel;

    @BindView(R.id.searchback)
    ImageButton searchBack;
    @BindView(R.id.search_view)
    SearchView searchView;
    @BindView(android.R.id.empty)
    ProgressBar progress;
    @BindView(R.id.search_results)
    RecyclerView searchRecyclerView;
    @BindView(R.id.container)
    ViewGroup container;
    @BindView(R.id.results_container)
    ViewGroup resultsContainer;
    @BindView(R.id.scrim)
    View scrim;
    @BindView(R.id.results_scrim)
    View resultsScrim;

    private SearchJobsAdapter searchJobsAdapter;
    private TextView noResults;
    private SparseArray<Transition> transitions = new SparseArray<>();

    private LinearLayoutManager linearLayoutManager;
    private List<Job> searchJobsList;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private Context context;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        searchViewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchViewModel.class);

        setupSearchView();

        linearLayoutManager = new LinearLayoutManager(this);
        searchJobsAdapter = new SearchJobsAdapter(this, context);

        searchRecyclerView.setAdapter(searchJobsAdapter);
        searchRecyclerView.setLayoutManager(linearLayoutManager);
        searchRecyclerView.setHasFixedSize(true);

        setupTransitions();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (searchRecyclerView != null) {
            postponeEnterTransition();
            searchRecyclerView.getViewTreeObserver()
                    .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            searchRecyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                            searchRecyclerView.requestLayout();
                            startPostponedEnterTransition();
                            return true;
                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        dismiss();
    }

    @Override
    protected void onPause() {
        ImeUtils.hideIme(searchView);
        overridePendingTransition(0, 0);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }

    @OnClick({R.id.scrim, R.id.searchback})
    protected void dismiss() {
        // clear the background else the touch ripple moves with the translation which looks bad
        searchBack.setBackground(null);
        finishAfterTransition();
    }

    @Override
    public void onEnterAnimationComplete() {
        boolean focusQuery = true;
        if (focusQuery) {
            // focus the search view once the enter transition finishes
            searchView.requestFocus();
            ImeUtils.showIme(searchView);
        }
    }

    private void setupSearchView() {
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(Objects.requireNonNull(searchManager).getSearchableInfo(getComponentName()));
        // hint, inputType & ime options seem to be ignored from XML! Set in code
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        searchView.setImeOptions(searchView.getImeOptions() | EditorInfo.IME_ACTION_SEARCH |
                EditorInfo.IME_FLAG_NO_EXTRACT_UI | EditorInfo.IME_FLAG_NO_FULLSCREEN);

        compositeDisposable.add(
                RxSearchObservable.fromView(searchView)
                        .debounce(300, TimeUnit.MILLISECONDS)
                        .filter(text -> text.length() > 0)
                        .distinctUntilChanged()
                        .switchMap((Function<String, ObservableSource<SearchResponse>>) query -> searchViewModel
                                .search(query)
                                .subscribeOn(Schedulers.io()))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(searchResponse -> {
                            progress.setVisibility(View.VISIBLE);
                            ImeUtils.hideIme(searchView);
                            searchView.clearFocus();

                            if (searchResponse.getResults() != null
                                    && searchResponse.getResults().size() > 0) {
                                searchJobsList = searchResponse.getResults();
                                Log.d(TAG, "Result:" + searchJobsList.get(0).getOriginalTitle());

                                if (searchRecyclerView.getVisibility() != View.VISIBLE) {
                                    setNoResultsVisibility(View.GONE);
                                    TransitionManager.beginDelayedTransition(container,
                                            getTransition(R.transition.search_show_results));
                                    progress.setVisibility(View.GONE);
                                    searchRecyclerView.setVisibility(View.VISIBLE);
                                    //fab.setVisibility(View.VISIBLE);
                                }
                                searchJobsAdapter.setSearchJobsList(searchJobsList);
                            } else {
                                TransitionManager.beginDelayedTransition(
                                        container, getTransition(R.transition.auto));
                                progress.setVisibility(View.GONE);
                                setNoResultsVisibility(View.VISIBLE);
                            }

                        }, throwable -> {
                            Log.d(TAG, throwable.getMessage());
                            clearResults();
                        }));

        searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            /*if (hasFocus && confirmSaveContainer.getVisibility() == View.VISIBLE) {
                //hideSaveConfirmation();
            }*/
        });

    }

    void searchFor(String query) {
        //clearResults();
        progress.setVisibility(View.VISIBLE);
        ImeUtils.hideIme(searchView);
        searchView.clearFocus();
        searchViewModel.search(query);
    }

    private void clearResults() {
        TransitionManager.beginDelayedTransition(container, getTransition(R.transition.auto));
        searchJobsList.clear();
        searchRecyclerView.setVisibility(View.GONE);
        progress.setVisibility(View.GONE);
        resultsScrim.setVisibility(View.GONE);
        setNoResultsVisibility(View.GONE);
    }

    private void setNoResultsVisibility(int visibility) {
        if (visibility == View.VISIBLE) {
            if (noResults == null) {
                noResults = (TextView) ((ViewStub)
                        findViewById(R.id.stub_no_search_results)).inflate();
                noResults.setOnClickListener(v -> {
                    searchView.setQuery("", false);
                    searchView.requestFocus();
                    ImeUtils.showIme(searchView);
                });
            }
            String message = String.format(
                    getString(R.string.no_search_results), searchView.getQuery().toString());
            SpannableStringBuilder ssb = new SpannableStringBuilder(message);
            ssb.setSpan(new StyleSpan(Typeface.ITALIC),
                    message.indexOf('“') + 1,
                    message.length() - 1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            noResults.setText(ssb);
        }
        if (noResults != null) {
            noResults.setVisibility(visibility);
        }
    }

    private void setupTransitions() {
        // grab the position that the search icon transitions in *from*
        // & use it to configure the return transition
        setEnterSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onSharedElementStart(
                    List<String> sharedElementNames,
                    List<View> sharedElements,
                    List<View> sharedElementSnapshots) {
                if (sharedElements != null && !sharedElements.isEmpty()) {
                    View searchIcon = sharedElements.get(0);
                    if (searchIcon.getId() != R.id.searchback) return;
                    int centerX = (searchIcon.getLeft() + searchIcon.getRight()) / 2;
                    CircularReveal hideResults = (CircularReveal) TransitionUtils.findTransition(
                            (TransitionSet) getWindow().getReturnTransition(),
                            CircularReveal.class, R.id.results_container);
                    if (hideResults != null) {
                        hideResults.setCenter(new Point(centerX, 0));
                    }
                }
            }
        });
    }

    private Transition getTransition(@TransitionRes int transitionId) {
        Transition transition = transitions.get(transitionId);
        if (transition == null) {
            transition = TransitionInflater.from(this).inflateTransition(transitionId);
            transitions.put(transitionId, transition);
        }
        return transition;
    }

}

