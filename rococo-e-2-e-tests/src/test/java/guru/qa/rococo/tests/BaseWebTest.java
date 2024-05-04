package guru.qa.rococo.tests;

import guru.qa.rococo.core.annotations.WebTest;
import guru.qa.rococo.page.MainPage;

@WebTest
public abstract class BaseWebTest {
    protected MainPage mainPage = new MainPage();
}
