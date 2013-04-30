package grouppractical.client.commands;

import grouppractical.utils.map.Position;

/**
 * <p>Immutable class representing an update to a position on the map.</p>
 * <p>Position can be in the range ±32767.</p>
 * @author jay
 *
 */
public class MPositionCommand implements Command {
	public static final int MIN_X = -16384, MIN_Y = -16384, MAX_X = 16384, MAX_Y = 16384;
	
	private final Position position;
	/**
	 * Constructs a new Position Command
	 * @param p Position which has updated
	 */
	public MPositionCommand(Position position) {
		this.position = position;
	}
	
	@Override
	public CommandType getCommandType() {
		return CommandType.MPOSITION;
	}

	@Override
	public char[] serialize() {
		char[] bytes = new char[6];
		//Command Type
		bytes[0] = getCommandType().toChar();
		
		int x = Math.abs(position.getX()), y = Math.abs(position.getY()), c = position.getCertainty();
		boolean x_negative = position.getX() < 0, y_negative = position.getY() < 0;
		
		//X coordinate
		bytes[1] = (char) ((0xFE & (x >> 7)) ^ (x_negative ? 0x01 : 0x00));
		bytes[2] = (char) (0xFF & x);
		//Y coordinate
		bytes[3] = (char) ((0xFE & (y >> 7)) ^ (y_negative ? 0x01 : 0x00));
		bytes[4] = (char) (0xFF & y);
		//Occupied and Certainty
		bytes[5] = (char) (((c & 0x7F) << 1) ^ (position.isOccupied() ? 0x01 : 0x00));
		
		return bytes;
	}
	
	/**
	 * Gets the position that has updated
	 * @return Position that has updated
	 */
	public Position getPosition() {
		return position;
	}
	
	@Override
	public String toString() {
		return String.format("MPOSITION(x: %d, y: %d, %f %% %s)", position.getX(), position.getY(),
				position.getCertaintyPercent(), position.isOccupied() ? "occupied" : "empty"); }
	
	@Override
	public boolean equals(Object that) {
		if (this == that) return true;
		if (that == null || !that.getClass().equals(MPositionCommand.class)) return false;
		MPositionCommand mthat = (MPositionCommand)that;
		return mthat.getPosition().equals(this.getPosition());
	}
	
	@Override
	public int hashCode() {
		return getPosition().hashCode();
	}
}
