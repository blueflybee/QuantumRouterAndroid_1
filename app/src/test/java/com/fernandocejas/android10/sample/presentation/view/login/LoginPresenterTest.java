package com.fernandocejas.android10.sample.presentation.view.login;

import android.content.Context;

import com.fernandocejas.android10.sample.domain.interactor.cloud.LoginUseCase;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.presentation.view.login.login.LoginPresenter;
import com.fernandocejas.android10.sample.presentation.view.login.login.LoginView;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.mapp.model.req.LoginRequest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rx.Subscriber;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/09
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class LoginPresenterTest {

    @Mock
    private LoginView loginView;

    @Mock
    private LoginUseCase mockLoginUseCase;

//    @Mock
//    private LoginListUseCase loginListUseCase;

    @Mock
    private Context mockContext;

//    private BaseSchedulerProvider mSchedulerProvider;

    private LoginPresenter loginPresenter;

    private QtecEncryptInfo<LoginRequest> request;

    @Before
    public void setupTasksPresenter() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Make the sure that all schedulers are immediate.
//        mSchedulerProvider = new ImmediateSchedulerProvider();

        // Get a reference to the class under test
        loginPresenter = new LoginPresenter(mockLoginUseCase);

        loginPresenter.setView(loginView);
        request = new QtecEncryptInfo<>();
        LoginRequest loginRequest = new LoginRequest();
        request.setData(loginRequest);

        // The presenter won't update the view unless it's active.
//        when(loginView.isActive()).thenReturn(true);

        // We subscribe the tasks to 3, with one active and two completed
//        TASKS = Lists.newArrayList(new Task("Title1", "Description1"),
//                new Task("Title2", "Description2", true), new Task("Title3", "Description3", true));
    }


//    @Test
//    public void testLogin() {
//        String username = "user";
//        String password = "123456";
//        request.getData().setUserPhone(username);
//        request.getData().setUserPassword(password);
//
//        when(loginView.getContext()).thenReturn(mockContext);
//        loginPresenter.login(request);
//
//        verify(loginView).showLoading();
//        verify(mockLoginUseCase).execute(any(IRequest.class), any(Subscriber.class));
//    }
//
//
//    @Test public void testUsernameEmp() {
//        String username = "";
//        String password = "123456";
//        request.getData().setUserPhone(username);
//        request.getData().setUserPassword(password);
//        when(loginView.getContext()).thenReturn(mockContext);
//        loginPresenter.login(request);
//
//        verify(loginView).showUsernameEmp();
//        verify(loginView, never()).showLoading();
//        verify(mockLoginUseCase, never()).execute(any(IRequest.class), any(Subscriber.class));
//    }
//
//    @Test public void testPasswordEmp() {
//        String username = "user";
//        String password = "";
//        request.getData().setUserPhone(username);
//        request.getData().setUserPassword(password);
//        when(loginView.getContext()).thenReturn(mockContext);
//        loginPresenter.login(request);
//
//        verify(loginView).showPasswordEmp();
//        verify(loginView, never()).showUsernameEmp();
//        verify(loginView, never()).showLoading();
//        verify(mockLoginUseCase, never()).execute(any(IRequest.class), any(Subscriber.class));
//    }

//    @Test
//    public void createPresenter_setsThePresenterToView() {
//        // Get a reference to the class under test
//        loginPresenter = new TasksPresenter(mTasksRepository, loginView, mSchedulerProvider);
//
//        // Then the presenter is set to the view
//        verify(loginView).setPresenter(loginPresenter);
//    }
//
//    @Test
//    public void loadAllTasksFromRepositoryAndLoadIntoView() {
//        // Given an initialized TasksPresenter with initialized tasks
//        when(mTasksRepository.getTasks()).thenReturn(Observable.just(TASKS));
//        // When loading of Tasks is requested
//        loginPresenter.setFiltering(TasksFilterType.ALL_TASKS);
//        loginPresenter.loadTasks(true);
//
//        // Then progress indicator is shown
//        verify(loginView).setLoadingIndicator(true);
//        // Then progress indicator is hidden and all tasks are shown in UI
//        verify(loginView).setLoadingIndicator(false);
//    }
//

//    @Test
//    public void loadActiveTasksFromRepositoryAndLoadIntoView() {
//        // Given an initialized TasksPresenter with initialized tasks
//        when(mTasksRepository.getTasks()).thenReturn(Observable.just(TASKS));
//        // When loading of Tasks is requested
//        loginPresenter.setFiltering(TasksFilterType.ACTIVE_TASKS);
//        loginPresenter.loadTasks(true);
//
//        // Then progress indicator is hidden and active tasks are shown in UI
//        verify(loginView).setLoadingIndicator(false);
//    }
//
//    @Test
//    public void loadCompletedTasksFromRepositoryAndLoadIntoView() {
//        // Given an initialized TasksPresenter with initialized tasks
//        when(mTasksRepository.getTasks()).thenReturn(Observable.just(TASKS));
//        // When loading of Tasks is requested
//        loginPresenter.setFiltering(TasksFilterType.COMPLETED_TASKS);
//        loginPresenter.loadTasks(true);
//
//        // Then progress indicator is hidden and completed tasks are shown in UI
//        verify(loginView).setLoadingIndicator(false);
//    }
//
//    @Test
//    public void clickOnFab_ShowsAddTaskUi() {
//        // When adding a new task
//        loginPresenter.addNewTask();
//
//        // Then add task UI is shown
//        verify(loginView).showAddTask();
//    }
//
//    @Test
//    public void clickOnTask_ShowsDetailUi() {
//        // Given a stubbed active task
//        Task requestedTask = new Task("Details Requested", "For this task");
//
//        // When open task details is requested
//        loginPresenter.openTaskDetails(requestedTask);
//
//        // Then task detail UI is shown
//        verify(loginView).showTaskDetailsUi(any(String.class));
//    }
//
//    @Test
//    public void completeTask_ShowsTaskMarkedComplete() {
//        // Given a stubbed task
//        Task task = new Task("Details Requested", "For this task");
//        // And no tasks available in the repository
//        when(mTasksRepository.getTasks()).thenReturn(Observable.<List<Task>>empty());
//
//        // When task is marked as complete
//        loginPresenter.completeTask(task);
//
//        // Then repository is called and task marked complete UI is shown
//        verify(mTasksRepository).completeTask(task);
//        verify(loginView).showTaskMarkedComplete();
//    }
//
//    @Test
//    public void activateTask_ShowsTaskMarkedActive() {
//        // Given a stubbed completed task
//        Task task = new Task("Details Requested", "For this task", true);
//        // And no tasks available in the repository
//        when(mTasksRepository.getTasks()).thenReturn(Observable.<List<Task>>empty());
//        loginPresenter.loadTasks(true);
//
//        // When task is marked as activated
//        loginPresenter.activateTask(task);
//
//        // Then repository is called and task marked active UI is shown
//        verify(mTasksRepository).activateTask(task);
//        verify(loginView).showTaskMarkedActive();
//    }
//
//    @Test
//    public void errorLoadingTasks_ShowsError() {
//        // Given that no tasks are available in the repository
//        when(mTasksRepository.getTasks()).thenReturn(Observable.<List<Task>>error(new Exception()));
//
//        // When tasks are loaded
//        loginPresenter.setFiltering(TasksFilterType.ALL_TASKS);
//        loginPresenter.loadTasks(true);
//
//        // Then an error message is shown
//        verify(loginView).showLoadingTasksError();
//    }
}