package no.vegvesen.vt.nvdb.commons.core;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class VersionNoTest {

    @Test
    public void shouldParseAndFormatVersionString() {
        assertParsedSameAsFormatted("1");
        assertParsedSameAsFormatted("1.2");
        assertParsedSameAsFormatted("1.2.3");
        assertParsedSameAsFormatted("1.123.123.123.0");
    }

    @Test
    public void shouldCompareToOther() {
        assertThat(v("1").compareTo(v("1")), is(0));
        assertThat(v("1.2").compareTo(v("1.2")), is(0));
        assertThat(v("1").compareTo(v("2")), is(-1));
        assertThat(v("2").compareTo(v("1")), is(1));
        assertThat(v("1.2.3").compareTo(v("1")), is(1));
        assertThat(v("1.2").compareTo(v("1.2.3")), is(-1));
    }

    private void assertParsedSameAsFormatted(String versionNo) {
        VersionNo v = VersionNo.parse(versionNo);
        String formatted = v.format();
        assertThat(formatted, is(versionNo));
    }

    private VersionNo v(String versionNo) {
        return VersionNo.parse(versionNo);
    }
}