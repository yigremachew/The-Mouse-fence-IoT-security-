import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.jasig.cas.authentication.ImmutableAuthentication;
import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.authentication.principal.SimplePrincipal;
import org.jasig.cas.authentication.principal.SimpleWebApplicationServiceImpl;
import org.jasig.cas.services.RegisteredService;
import org.jasig.cas.ticket.support.NeverExpiresExpirationPolicy;
import org.openiot.lsm.security.oauth.LSMOAuthHttpManager;
import org.openiot.lsm.security.oauth.LSMRegisteredServiceImpl;
import org.openiot.lsm.security.oauth.LSMServiceTicketImpl;
import org.openiot.lsm.security.oauth.LSMTicketGrantingTicketImpl;
import org.openiot.lsm.security.oauth.mgmt.Permission;
import org.openiot.lsm.security.oauth.mgmt.Role;
import org.openiot.lsm.security.oauth.mgmt.User;
import org.openiot.security.oauth.lsm.LSMOAuthManager;

public class TestLSMOAuthentication {
	static String OAuthGraphURL = "http://lsm.deri.ie/OpenIoT/OAuth#";

	public static User generateUser(String name, String email, String username, String password) {
		User user = new User();
		user.setName(name);
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword(password);

		return user;
	}

	public static Role generateRole() {
		Role role = new Role();
		role.setDescription("Administrator role");
		role.setName("admin");
		role.setServiceId(1L);

		Permission per = new Permission();
		per.setDescription("Create new users");
		per.setName("admin:create_user");
		per.setServiceId(1L);
		role.addPermission(per);

		Permission per2 = new Permission();
		per2.setDescription("Delete stream s1");
		per2.setName("admin:delete_sensor:s1");
		per.setServiceId(1L);
		role.addPermission(per2);

		Permission per3 = new Permission();
		per3.setDescription("Delete existing users");
		per3.setName("admin:delete_user");
		per.setServiceId(1L);
		role.addPermission(per3);

		return role;

	}

	/**
	 * "*","All permissions" "admin:create_user","Create new users"
	 * "admin:delete_sensor:s1","Delete stream s1"
	 * "admin:delete_sensor:s2,s3","Delete streams s2 and s3"
	 * "admin:delete_user","Delete existing users" "sensor:discover:s1","View stream s1"
	 * "sensor:discover:s2","View stream s2" "sensor:query:s1","Query stream s1"
	 * "sensor:query:s2","Query stream s2"
	 * 
	 * @return
	 */
	public static void generateAuthorizationData() {
		LSMOAuthHttpManager oM = new LSMOAuthHttpManager(OAuthGraphURL);
		User adminUser = generateUser("Administrator", "admin@example.com", "admin", "5ebe2294ecd0e0f08eab7690d2a6ee69");
		User darkHelmetUser = generateUser("User P2", "darkh@example.com", "darkhelmet", "d9aaefa96ffeabb3a3bac5fdeadde3fa");
		User lonestarrUser = generateUser("User P3", "lonestarr@example.com", "lonestarr", "960c8c80adfcc7eee97eb6ebad135642");
		User presidentskroobUser = generateUser("User P1", "prskroob@example.com", "presidentskroob", "827ccb0eea8a706c4c34a16891f84e7b");

		User[] users = new User[] { adminUser, darkHelmetUser, lonestarrUser, presidentskroobUser };

//		Role adminRole = new Role("admin", "Administrator role", 1L);
		Role adminRole5 = new Role("admin", "Administrator role", 5L);
//		Role endUserRole = new Role("end_user", "End user role", 3L);
//		Role schedulerRole = new Role("scheduler", "Scheduler role", 3L);
//		Role serviceDefinerRole3 = new Role("service_definer", "Service definer role", 3L);
//		Role serviceDefinerRole4 = new Role("service_definer", "Service definer role", 4L);
//		Role visualizerRole = new Role("visualizer", "Data visualizer role", 3L);
//
//		Role[] roles = new Role[] { adminRole, adminRole3, endUserRole, schedulerRole, serviceDefinerRole3, serviceDefinerRole4, visualizerRole };
//
//		Permission allPerm = new Permission("*", "All permissions", 1L);
		Permission allPerm5 = new Permission("*", "All permissions", 5L);
		Permission createPermissionPerm5 = new Permission("admin:create_permissions", "Create new permissions", 5L);
//		Permission adminDeleteSens2and3Perm3 = new Permission("admin:delete_sensor:s2,s3", "Delete streams s2 and s3", 3L);
//		Permission sensorQuery1Perm3 = new Permission("sensor:query:s1", "Query stream s1", 3L);
//		Permission sensorQuery2Perm4 = new Permission("sensor:query:s2", "Query stream s2", 4L);
//
//		Permission[] permissions = new Permission[] { allPerm, allPerm3, adminDeleteSens2and3Perm3, sensorQuery1Perm3, sensorQuery2Perm4 };
//
		adminRole5.addPermission(allPerm5);
		adminRole5.addPermission(createPermissionPerm5);
//		adminRole3.addPermission(allPerm3);
//		serviceDefinerRole3.addPermission(adminDeleteSens2and3Perm3);
//		serviceDefinerRole3.addPermission(sensorQuery1Perm3);
//		serviceDefinerRole4.addPermission(sensorQuery2Perm4);
//
		adminUser.addRole(adminRole5);
//		presidentskroobUser.addRole(serviceDefinerRole3);
//		darkHelmetUser.setRoles(Arrays.asList(new Role[] { schedulerRole, endUserRole }));
//		lonestarrUser.setRoles(Arrays.asList(new Role[] { visualizerRole, endUserRole, serviceDefinerRole4 }));
//
//
//		for (Permission perm : permissions)
//			oM.addPermission(perm);
//
//		for (Role role : roles)
//			oM.addRole(role);

		oM.addPermission(allPerm5);
		oM.addPermission(createPermissionPerm5);
		
		oM.addRole(adminRole5);
		
		for (User user : users)
			oM.addUser(user);
//		oM.addPermission(allPerm5);
//		oM.addRole(adminRole5);
//		oM.addUser(adminUser);

	}

	public static List<LSMRegisteredServiceImpl> createDefaultServices() {
		LSMRegisteredServiceImpl defaultService = new LSMRegisteredServiceImpl();
		defaultService.setId(1L);
		defaultService.setAllowedToProxy(true);
		defaultService.setAnonymousAccess(false);
		defaultService.setDescription("Service Manager");
		defaultService.setEnabled(true);
		defaultService.setEvaluationOrder(0);
		defaultService.setIgnoreAttributes(true);
		defaultService.setName("Service Manager");
		defaultService.setServiceId("https://localhost:8443/openiot-cas/services/j_acegi_cas_security_check");
		defaultService.setSsoEnabled(true);

		LSMRegisteredServiceImpl httpService = new LSMRegisteredServiceImpl();
		httpService.setId(2L);
		httpService.setAllowedToProxy(true);
		httpService.setAnonymousAccess(false);
		httpService.setDescription("OAuth wrapper callback url");
		httpService.setEnabled(true);
		httpService.setEvaluationOrder(0);
		httpService.setIgnoreAttributes(true);
		httpService.setName("HTTP");
		httpService.setServiceId("https://localhost:8443/openiot-cas/oauth2.0/callbackAuthorize");
		httpService.setSsoEnabled(true);

		LSMRegisteredServiceImpl oauthTestService1 = new LSMRegisteredServiceImpl();
		oauthTestService1.setId(3L);
		oauthTestService1.setAllowedToProxy(true);
		oauthTestService1.setAnonymousAccess(false);
		oauthTestService1.setDescription("testsecret1");
		oauthTestService1.setEnabled(true);
		oauthTestService1.setEvaluationOrder(0);
		oauthTestService1.setIgnoreAttributes(false);
		oauthTestService1.setName("testservice1");
		oauthTestService1.setServiceId("http://localhost:9080/callback?client_name=CasOAuthWrapperClient");
		oauthTestService1.setTheme("Service1");
		oauthTestService1.setSsoEnabled(true);

		LSMRegisteredServiceImpl oauthTestService2 = new LSMRegisteredServiceImpl();
		oauthTestService2.setId(4L);
		oauthTestService2.setAllowedToProxy(true);
		oauthTestService2.setAnonymousAccess(false);
		oauthTestService2.setDescription("testsecret2");
		oauthTestService2.setEnabled(true);
		oauthTestService2.setEvaluationOrder(0);
		oauthTestService2.setIgnoreAttributes(false);
		oauthTestService2.setName("testservice2");
		oauthTestService2.setServiceId("http://localhost:7080/callback?client_name=CasOAuthWrapperClient");
		oauthTestService2.setTheme("Service2");
		oauthTestService2.setSsoEnabled(true);

		return Arrays.asList(new LSMRegisteredServiceImpl[] { defaultService, httpService, oauthTestService1, oauthTestService2 });
	}

	public static LSMRegisteredServiceImpl createRegisteredService() {
		LSMRegisteredServiceImpl reg_ser = new LSMRegisteredServiceImpl();
		reg_ser.setId((long) 1);
		reg_ser.setAllowedToProxy(true);
		reg_ser.setAnonymousAccess(true);
		reg_ser.setDescription("Service Manager");
		reg_ser.setEnabled(true);
		reg_ser.setEvaluationOrder(0);
		reg_ser.setIgnoreAttributes(false);
		reg_ser.setName("Service Manager");
		reg_ser.setServiceId("https://localhost:8443/openiot-cas/services/j_acegi_cas_security_check");
		reg_ser.setSsoEnabled(false);
		return reg_ser;
	}

	public static LSMRegisteredServiceImpl createDefaultCASService() {
		LSMRegisteredServiceImpl reg_ser = new LSMRegisteredServiceImpl();
		reg_ser.setId((long) 1);
		reg_ser.setAllowedToProxy(true);
		reg_ser.setAnonymousAccess(false);
		reg_ser.setDescription("Service Manager");
		reg_ser.setEnabled(true);
		reg_ser.setEvaluationOrder(0);
		reg_ser.setIgnoreAttributes(false);
		reg_ser.setName("Service Manager");
		reg_ser.setServiceId("https://localhost:8443/openiot-cas/services/j_acegi_cas_security_check");
		reg_ser.setSsoEnabled(true);
		return reg_ser;
	}

	public static LSMRegisteredServiceImpl createDefaultHTTPService() {
		LSMRegisteredServiceImpl reg_ser = new LSMRegisteredServiceImpl();
		reg_ser.setId((long) 2);
		reg_ser.setAllowedToProxy(true);
		reg_ser.setAnonymousAccess(false);
		reg_ser.setDescription("OAuth wrapper callback url");
		reg_ser.setEnabled(true);
		reg_ser.setEvaluationOrder(0);
		reg_ser.setIgnoreAttributes(false);
		reg_ser.setName("HTTP");
		reg_ser.setServiceId("https://localhost:8443/openiot-cas/oauth2.0/callbackAuthorize");
		reg_ser.setSsoEnabled(true);
		return reg_ser;
	}

	public static LSMTicketGrantingTicketImpl createTicketGrantingTicket() {
		LSMTicketGrantingTicketImpl tgt = new LSMTicketGrantingTicketImpl();
		tgt.setId("TGT-1-0VPQMgR6P4OeVdAFK3O3CWxkKUZUaZlRPnEOOw9qIVIhj6tP9A-openiot.eu");
		tgt.setCountOfUses(1);
		tgt.setCreationTime(1385137975212L);
		tgt.setExpirationPolicy(new NeverExpiresExpirationPolicy());
		tgt.setLastTimeUsed(1385137975237L);
		tgt.setPreviousLastTimeUsed(1385137975212L);
		tgt.setExpired(false);
		tgt.setAuthentication(new ImmutableAuthentication(new SimplePrincipal("sp-id")));
		final HashMap<String, Service> services = new HashMap<String, Service>();
		services.put("dummy-id", new SimpleWebApplicationServiceImpl("dummy-id"));
		tgt.setServices(services);

		tgt.setTicketGrantingTicket(null);
		return tgt;
	}

	public static LSMTicketGrantingTicketImpl createTicketGrantingTicket(LSMTicketGrantingTicketImpl grantingTicket) {
		LSMTicketGrantingTicketImpl tgt = new LSMTicketGrantingTicketImpl();
		tgt.setId("TGT-2-eLPogT3jXcUd1chRvtqKH6Rxv49XbtXZfEp5qoR1ynhbGOlxIK-openiot.eu");
		tgt.setCountOfUses(2);
		tgt.setCreationTime(1385138057030L);
		tgt.setExpirationPolicy(new NeverExpiresExpirationPolicy());
		tgt.setLastTimeUsed(1385138114044L);
		tgt.setPreviousLastTimeUsed(1385138057063L);
		tgt.setExpired(false);
		tgt.setAuthentication(new ImmutableAuthentication(new SimplePrincipal("sp-id2")));
		final HashMap<String, Service> services = new HashMap<String, Service>();
		services.put("dummy-id2", new SimpleWebApplicationServiceImpl("dummy-id2"));
		tgt.setServices(services);
		tgt.setTicketGrantingTicket(grantingTicket);
		return tgt;
	}

	public static LSMServiceTicketImpl createServiceTicket(LSMTicketGrantingTicketImpl grantingTicket) {
		LSMServiceTicketImpl serviceTicket = new LSMServiceTicketImpl();
		serviceTicket.setId("ST-4-BF7u5cS33WUK5UJOoZyA-openiot.eu");
		serviceTicket.setCountOfUses(0);
		serviceTicket.setCreationTime(1385138114044L);
		serviceTicket.setExpirationPolicy(new NeverExpiresExpirationPolicy());
		serviceTicket.setLastTimeUsed(1385138114044L);
		serviceTicket.setPreviousLastTimeUsed(0L);
		serviceTicket.setFromNewLogin(false);
		serviceTicket.setService(new SimpleWebApplicationServiceImpl("dummy-id3"));
		serviceTicket.setTicketGrantingTicket(grantingTicket);
		return serviceTicket;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LSMOAuthManager oM = LSMOAuthManager.getInstance();

//		 for(LSMRegisteredServiceImpl rs : createDefaultServices())
//		 oM.addRegisteredService(rs);
//		
		 generateAuthorizationData();
		
		User userByUsername = oM.getUserByUsername("admin");
		System.out.println(userByUsername);
		
		System.out.println(userByUsername.getRoles().size());
		System.out.println(userByUsername.getRoles().get(0));
		
		System.out.println("Test____Role".matches(".*(\\s|__|/).*"));

		// final LSMRegisteredServiceImpl registeredService = oM.getRegisteredService(100L);
		// System.out.println("getUsernameAttribute() is null: " +
		// registeredService.getUsernameAttribute() == null);
		// System.out.println("getTheme(): " + registeredService.getTheme());
		// System.out.println("getTheme() is null: " + registeredService.getTheme() == null);
		// System.out.println("getTheme().equals(\"null\"): " +
		// "null".equals(registeredService.getTheme()));
		// System.out.println("isAnonymousAccess(): " + registeredService.isAnonymousAccess());

		if (true)
			return;

		LSMTicketGrantingTicketImpl realTicket = oM.getTicketGranting("TGT-5-9ynOlGGcYiJxaQZxZpZceNgsmKLF5pXOUQadLhV7otqeQaUM9P-openiot.eu");
		if (realTicket != null) {
			System.out.println("LastTimeUsed: " + realTicket.getCreationTime() + " | " + new Date(realTicket.getCreationTime()));
			System.out.println("isExpired(): " + realTicket.isExpired());
			System.out.println("isExpiredInternal(): " + realTicket.isExpiredInternal());
			System.out.println("getExpirationPolicy(): " + realTicket.getExpirationPolicy().getClass());
			System.out.println("getExpirationPolicy(): " + realTicket.getAuthentication().getPrincipal().getId());
		}

		// oM.addRegisteredService(createDefaultCASService());

		LSMRegisteredServiceImpl service = oM.getRegisteredService((long) 1);
		System.out.println(service.getUsernameAttribute() == null);
		System.out.println("isAnonymousAccess(): " + service.isAnonymousAccess());

		if (true)
			return;

		// oM.addPermission(generatePermission("admin:create_user","Create new users"));
		// oM.deletePermission("admin:create_user");

		// oM.addRole(generateRole());
		// oM.deleteRole("admin");

		// oM.addUser(generateOAuthUser());
		// oM.deleteUser("admin");

		// oM.addRegisteredService(createRegisteredService());
		// oM.deleteRegisteredService(1);

		// User user = oM.getUser("admin");
		// System.out.println(user.getRoles().size());
		// LSMRegisteredServiceImpl service = oM.getRegisteredService((long)1);
		// System.out.println(service.getId());

		final LSMTicketGrantingTicketImpl tgt = createTicketGrantingTicket();

		// oM.addTicketGrangtingTicket(tgt);
		final LSMTicketGrantingTicketImpl ticketGranting = oM.getTicketGranting(tgt.getId());
		System.out.println(ticketGranting);
		// oM.deleteTicketGranting(tgt.getId());
		// final LSMTicketGrantingTicketImpl ticketGranting2 = oM.getTicketGranting(tgt.getId());
		// System.out.println(ticketGranting2);

		final LSMTicketGrantingTicketImpl tgt2 = createTicketGrantingTicket(tgt);
		// oM.addTicketGrangtingTicket(tgt2);
		final LSMTicketGrantingTicketImpl ticketGranting2 = oM.getTicketGranting(tgt2.getId());
		System.out.println(ticketGranting2);
		if (ticketGranting2 != null)
			System.out.println(ticketGranting2.getGrantingTicket());

		// oM.deleteTicketGranting(tgt2.getId());
		// final LSMTicketGrantingTicketImpl ticketGranting22 = oM.getTicketGranting(tgt2.getId());
		// System.out.println(ticketGranting22);

		final LSMServiceTicketImpl serviceTicket = createServiceTicket(tgt);
		// oM.addServiceTicketImpl(serviceTicket);

		LSMServiceTicketImpl serviceTicket2 = oM.getServiceTicketImpl(serviceTicket.getId());
		System.out.println(serviceTicket2);

		// oM.deleteServiceTicketImpl(serviceTicket.getId());
		// serviceTicket2 = oM.getServiceTicketImpl(serviceTicket.getId());
		// System.out.println(serviceTicket2);
		//
		// oM.deleteTicketGranting(tgt2.getId());

		final List<LSMTicketGrantingTicketImpl> allTkts = oM.getAllTicketGrantingTickets();
		System.out.println(allTkts.size());
		if (!allTkts.isEmpty())
			System.out.println(allTkts.get(0));

		final List<LSMServiceTicketImpl> allTktsOf = oM.getAllServiceTicketsOfTicketGrantingTicket(tgt.getId());
		System.out.println("allTktsOf size: " + allTktsOf.size());
		if (!allTktsOf.isEmpty())
			System.out.println(allTktsOf.get(0));

		final List<LSMTicketGrantingTicketImpl> allGrantingTktsOf = oM.getAllTicketsOfTicketGrantingTicket(tgt.getId());
		System.out.println("allGrantingTktsOf size: " + allGrantingTktsOf.size());
		if (!allGrantingTktsOf.isEmpty())
			System.out.println(allGrantingTktsOf.get(0));

		final List<LSMServiceTicketImpl> allServiceTkts = oM.getAllServiceTickets();
		System.out.println("allServiceTkts size: " + allServiceTkts.size());
		if (!allServiceTkts.isEmpty())
			System.out.println(allServiceTkts.get(0));

		final User user = userByUsername;
		if (user != null) {
			System.out.println(user.getUsername());
			System.out.println(user.getRoles().size());
		} else
			System.out.println("User admin is not found");

		final List<RegisteredService> allRegisteredServices = oM.getAllRegisteredServices();
		System.out.println("All registered services size:" + allRegisteredServices.size());
		if (!allRegisteredServices.isEmpty())
			System.out.println(allRegisteredServices.get(0));

		final int ticketGrantingTicketsCount = oM.getTicketGrantingTicketsCount();
		System.out.println("ticketGrantingTicketsCount: " + ticketGrantingTicketsCount);

		final int serviceTicketsCount = oM.getServiceTicketsCount();
		System.out.println("serviceTicketsCount: " + serviceTicketsCount);
	}

}
