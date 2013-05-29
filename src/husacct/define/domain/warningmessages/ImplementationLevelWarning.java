package husacct.define.domain.warningmessages;

import java.util.Observable;

import husacct.define.domain.module.Module;

public class ImplementationLevelWarning extends WarningMessage {

	private Module module;

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	public ImplementationLevelWarning(Module module) {
		this.module = module;
		generateMessage();
	}

	@Override
	public void generateMessage() {
		this.description = "A module must be mapped to an implementation unit";
		this.resource = "Module name: " + module.getName();
		this.type = "Implentation Level";
		this.location = "";

	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object[] getValue() {
		// TODO Auto-generated method stub
		return new Object[]{module};
	}
}