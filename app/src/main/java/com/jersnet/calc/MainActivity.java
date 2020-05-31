package com.jersnet.calc;

import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.*;
import android.view.View.*;
import java.math.*;

public class MainActivity extends Activity implements View.OnClickListener
{


	Button button1,button2,button3,button4,button5,button6,button7,button8,button9,buttonzero;
	Button button_ac,button_eq,button_div,button_add,button_sub,button_mul;
	EditText edittext;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		// initialize textscreen
		edittext = findViewById(R.id.mainEditText1);
		
		// initialize button numbers
		button1 = findViewById(R.id.b1);
		button2 = findViewById(R.id.b2);
		button3 = findViewById(R.id.b3);
		button4 = findViewById(R.id.b4);
		button5 = findViewById(R.id.b5);
		button6 = findViewById(R.id.b6);
		button7 = findViewById(R.id.b7);
		button8 = findViewById(R.id.b8);
		button9 = findViewById(R.id.b9);
		buttonzero = findViewById(R.id.b0);
		
		// initialize operands
		button_add = findViewById(R.id.badd);
		button_sub = findViewById(R.id.bsub);
		button_div = findViewById(R.id.bdiv);
		button_mul = findViewById(R.id.bmul);
		button_eq = findViewById(R.id.beq);
		button_ac = findViewById(R.id.bac);
		
		//set listeners
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		button3.setOnClickListener(this);
		button4.setOnClickListener(this);
		button5.setOnClickListener(this);
		button6.setOnClickListener(this);
		button7.setOnClickListener(this);
		button8.setOnClickListener(this);
		button9.setOnClickListener(this);
		buttonzero.setOnClickListener(this);
		
		button_add.setOnClickListener(this);
		button_ac.setOnClickListener(this);
		button_sub.setOnClickListener(this);
		button_mul.setOnClickListener(this);
		button_div.setOnClickListener(this);
		button_eq.setOnClickListener(this);
    }
	
	@Override
	public void onClick(View view)
	{
		if(view == button1) {
			edittext.append("1");
		}else if(view == button2) {
			edittext.append("2");
		}else if(view == button3) {
			edittext.append("3");
		}else if(view == button4) {
			edittext.append("4");
		}else if(view == button5) {
			edittext.append("5");
		}else if(view == button6) {
			edittext.append("6");
		}else if(view == button7) {
			edittext.append("7");
		}else if(view == button8) {
			edittext.append("8");
		}else if(view == button9) {
			edittext.append("9");
		}else if(view == buttonzero) {
			edittext.append("0");
		}
		
		if(view == button_add) {
			edittext.append(" + ");
		} else if(view == button_sub) {
			edittext.append(" - ");
		} else if(view == button_mul) {
			edittext.append(" x ");
		} else if(view == button_div) {
			edittext.append(" / ");
		} else if(view == button_ac) {
			edittext.setText(null);
		} else if(view == button_eq) {
			   // store temporary appended data fromt this variables
			    String tmp = edittext.getText().toString().trim();
				
				// replace x to * inorder to evaluate the expression
				String tmpx = tmp.replace("x","*");
				
				// set to input the final result and the appended data
		        edittext.setText(tmp + " = " + String.valueOf(eval(tmpx)));
		}
	}
	
	
	// This is the code evaluate the expression, this is just like javascript eval()
	public static double eval(final String str) {
		return new Object() {
			int pos = -1, ch;
			void nextChar() {
				ch = (++pos < str.length()) ? str.charAt(pos) : -1;
			}
			boolean eat(int charToEat) {
				while (ch == ' ') nextChar();
				if (ch == charToEat) {
					nextChar();
					return true;
				}
				return false;
			}

			double parse() {
				nextChar();
				double x = parseExpression();
				if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
				return x;
			}
			double parseExpression() {
				double x = parseTerm();
				for (;;) {
					if      (eat('+')) x += parseTerm(); // addition
					else if (eat('-')) x -= parseTerm(); // subtraction
					else return x;
				}
			}

			double parseTerm() {
				double x = parseFactor();
				for (;;) {
					if      (eat('*')) x *= parseFactor(); // multiplication
					else if (eat('/')) x /= parseFactor(); // division
					else return x;
				}
			}

			double parseFactor() {
				if (eat('+')) return parseFactor(); // unary plus
				if (eat('-')) return -parseFactor(); // unary minus

				double x;
				int startPos = this.pos;
				if (eat('(')) { // parentheses
					x = parseExpression();
					eat(')');
				} else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
					while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
					x = Double.parseDouble(str.substring(startPos, this.pos));
				} else if (ch >= 'a' && ch <= 'z') { // functions
					while (ch >= 'a' && ch <= 'z') nextChar();
					String func = str.substring(startPos, this.pos);
					x = parseFactor();
					if (func.equals("sqrt")) x = Math.sqrt(x);
					else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
					else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
					else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
					else throw new RuntimeException("Unknown function: " + func);
				} else {
					throw new RuntimeException("Unexpected: " + (char)ch);
				}

				if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

				return x;
			}
		}.parse();
	}
	
}
