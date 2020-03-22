import play.api.mvc.EssentialFilter;
import play.filters.cors.CORSFilter;
import play.filters.headers.SecurityHeadersFilter;
import play.http.HttpFilters;

import javax.inject.Inject;

public class Filters implements HttpFilters {

    @Inject
    SecurityHeadersFilter securityHeadersFilter;
    @Inject
    CORSFilter corsFilter;

    public EssentialFilter[] filters() {
        return new EssentialFilter[] { corsFilter, securityHeadersFilter };
    }
}
