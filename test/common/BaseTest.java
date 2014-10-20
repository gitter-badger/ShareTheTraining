package common;

import static play.test.Helpers.inMemoryDatabase;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;

import play.db.jpa.JPA;
import play.db.jpa.JPAPlugin;
import play.test.FakeApplication;
import play.test.Helpers;
import scala.Option;

public abstract class BaseTest {
	
	public Initialization initData;
	
	@Before
	public void setUp() {

		final FakeApplication app = Helpers.fakeApplication(inMemoryDatabase());
		Helpers.start(app);
		Option<JPAPlugin> jpaPlugin = app.getWrappedApplication().plugin(
				JPAPlugin.class);
		mEm = jpaPlugin.get().em("default");
		JPA.bindForCurrentThread(mEm);
		initData = new Initialization(mEm);
	}

	@After
	public void tearDown() {

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
}
