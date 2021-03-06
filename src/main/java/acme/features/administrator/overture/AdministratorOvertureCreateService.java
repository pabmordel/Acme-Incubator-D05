
package acme.features.administrator.overture;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.overture.Overture;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Administrator;
import acme.framework.services.AbstractCreateService;

@Service
public class AdministratorOvertureCreateService implements AbstractCreateService<Administrator, Overture> {

	@Autowired
	AdministratorOvertureRepository repository;


	@Override
	public boolean authorise(final Request<Overture> request) {
		assert request != null;

		return true;
	}

	@Override
	public void bind(final Request<Overture> request, final Overture entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		request.bind(entity, errors);
	}

	@Override
	public void unbind(final Request<Overture> request, final Overture entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "title", "deadline", "description", "intervalmin", "intervalmax", "email");
	}

	@Override
	public Overture instantiate(final Request<Overture> request) {
		Overture result;

		result = new Overture();

		result.setCreationdate(new Date(System.currentTimeMillis() - 1));

		return result;
	}

	@Override
	public void validate(final Request<Overture> request, final Overture entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		if (!errors.hasErrors("deadline")) {
			Date now = new Date(System.currentTimeMillis());
			if (entity.getDeadline() == null) {
				errors.state(request, true, "deadline", "javax.validation.constraints.NotBlank.message");
			} else if (entity.getDeadline() != null) {
				errors.state(request, entity.getDeadline().after(now), "deadline", "acme.validation.deadline");
			}
		}

		if (entity.getIntervalmin() != null) {
			errors.state(request, entity.getIntervalmin().getCurrency().contains("€"), "intervalmin", "acme.validation.money");
		}

		if (entity.getIntervalmax() != null) {
			errors.state(request, entity.getIntervalmax().getCurrency().contains("€"), "intervalmax", "acme.validation.money");
		}
	}

	@Override
	public void create(final Request<Overture> request, final Overture entity) {
		this.repository.save(entity);
	}

}
