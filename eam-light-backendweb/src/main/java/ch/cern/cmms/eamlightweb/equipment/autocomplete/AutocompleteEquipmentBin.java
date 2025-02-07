package ch.cern.cmms.eamlightweb.equipment.autocomplete;

import java.util.Arrays;

import javax.enterprise.context.RequestScoped;
import javax.interceptor.Interceptors;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import ch.cern.cmms.eamlightweb.tools.autocomplete.Autocomplete;
import ch.cern.cmms.eamlightweb.tools.autocomplete.SimpleGridInput;
import ch.cern.cmms.eamlightweb.tools.interceptors.RESTLoggingInterceptor;
import ch.cern.eam.wshub.core.services.grids.entities.GridRequestFilter;
import ch.cern.eam.wshub.core.tools.InforException;

@Path("/autocomplete")
@RequestScoped
@Interceptors({ RESTLoggingInterceptor.class })
public class AutocompleteEquipmentBin extends Autocomplete {

	private SimpleGridInput prepareInput() {
		SimpleGridInput in = new SimpleGridInput("111", "LVSTRBIN", "112");
		in.setGridType("LOV");
		in.setFields(Arrays.asList("479", "480")); // 479 bincode
													// 480 bindescription
		return in;
	}

	@GET
	@Path("/eqp/bin")
	@Produces("application/json")
	@Consumes("application/json")
	public Response complete(@QueryParam("code") String code, @QueryParam("store") String store) {
		SimpleGridInput in = prepareInput();
		in.getGridFilters().add(new GridRequestFilter("bincode", code.toUpperCase(), "BEGINS"));
		in.getInforParams().put("bincodetohide", null);
		in.getInforParams().put("bisstore", store);
		in.getSortParams().put("bincode", true); // true=ASC, false=DESC

		try {
			return ok (getGridResults(in));
		} catch (InforException e) {
			return badRequest(e);
		} catch(Exception e) {
			return serverError(e);
		}
	}

}