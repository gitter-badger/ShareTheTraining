package common;

import static org.mockito.Mockito.*;
import static play.test.Helpers.inMemoryDatabase;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;
import org.mockito.Mock;

import play.api.mvc.Request;
import play.db.jpa.JPA;
import play.db.jpa.JPAPlugin;
import play.i18n.Lang;
import play.mvc.Http;
import play.test.FakeApplication;
import play.test.Helpers;
import scala.Option;

public abstract class BaseTest {

	public Initialization initData;

	@Mock
	private Http.Request request;

	@Before
	public void setUp() {

		final FakeApplication app = Helpers.fakeApplication();
		Helpers.start(app);
		Option<JPAPlugin> jpaPlugin = app.getWrappedApplication().plugin(
				JPAPlugin.class);
		mEm = jpaPlugin.get().em("default");
		JPA.bindForCurrentThread(mEm);
		mEm.getTransaction().begin();
		initData = new Initialization(mEm);
	}

	@After
	public void tearDown() {
		initData.dispose();
		mEm.getTransaction().commit();
		JPA.bindForCurrentThread(null);
		this.mEm.close();

	}

	private EntityManager mEm;

	public EntityManager getmEm() {
		return mEm;
	}

	public void setmEm(EntityManager mEm) {
		this.mEm = mEm;
	}

	public Http.Context getMockContext() {
		Map<String, String> flashData = Collections.emptyMap();
		Map<String, Object> argData = Collections.emptyMap();
		Long id = 2L;
	    play.api.mvc.RequestHeader header = mock(play.api.mvc.RequestHeader.class);
		Http.Context context = new Http.Context(id, header,request, flashData, flashData, argData);
		return context;
		
	}
}
