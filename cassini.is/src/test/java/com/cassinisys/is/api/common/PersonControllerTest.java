package com.cassinisys.is.api.common;

import com.cassinisys.is.BaseMockitoTest;
import com.cassinisys.is.TestUtil;
import com.cassinisys.platform.api.common.PersonController;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.service.common.PersonService;
import com.cassinisys.platform.util.PageRequestConverter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author reddy
 */
public class PersonControllerTest extends BaseMockitoTest {

	@Mock
	private PersonService personService;

	@Spy
	private PageRequestConverter pageConverter = new PageRequestConverter();

	@InjectMocks
	private PersonController personController;

	private MockMvc mockMvc;

	public PersonControllerTest() {
	}

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(personController).build();
	}

	@Test
	public void getAllTest() throws Exception {
		Person first = new Person();
		first.setId(1);
		first.setFirstName("person1");
		first.setLastName("Person1");
		Person second = new Person();
		second.setId(2);
		second.setFirstName("person2");
		second.setLastName("Person2");

		List<Person> content = new ArrayList<Person>();
		content.add(first);
		content.add(second);
		Page<Person> page = new PageImpl<Person>(content);

		when(personService.findAll(any(Pageable.class))).thenReturn(page);

		mockMvc.perform(get("/people?page=0&size=2"))
				.andExpect(status().isOk())
				.andExpect(
						content().contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.content", hasSize(2)))
				.andExpect(jsonPath("$.content[0].id", is(1)))
				.andExpect(jsonPath("$.content[0].firstName", is("person1")))
				.andExpect(jsonPath("$.content[0].lastName", is("Person1")))
				.andExpect(jsonPath("$.content[1].id", is(2)))
				.andExpect(jsonPath("$.content[1].firstName", is("person2")))
				.andExpect(jsonPath("$.content[1].lastName", is("Person2")));
		;
	}
}
