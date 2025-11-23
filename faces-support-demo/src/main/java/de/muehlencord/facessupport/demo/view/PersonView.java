package de.muehlencord.facessupport.demo.view;

import de.muehlencord.facessupport.LongSpringLazyDataModel;
import de.muehlencord.facessupport.SessionView;
import de.muehlencord.facessupport.demo.entity.Person;
import de.muehlencord.facessupport.demo.repository.PersonRepository;
import de.muehlencord.facessupport.view.AbstractStandardView;

/**
 * demo backing bean
 *
 * @author Joern Muehlencord, 2025-11-21
 * @since 0.1.0
 */
public class PersonView extends AbstractStandardView<Person, Long, PersonRepository> {

  public PersonView(SessionView sessionView, PersonRepository repository) {
    super("person", sessionView, new LongSpringLazyDataModel<>(repository), Person.class);
  }
}
