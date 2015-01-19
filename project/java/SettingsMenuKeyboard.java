/*
Simple DirectMedia Layer
Java source code (C) 2009-2014 Sergii Pylypenko

This software is provided 'as-is', without any express or implied
warranty.  In no event will the authors be held liable for any damages
arising from the use of this software.

Permission is granted to anyone to use this software for any purpose,
including commercial applications, and to alter it and redistribute it
freely, subject to the following restrictions:

1. The origin of this software must not be misrepresented; you must not
   claim that you wrote the original software. If you use this software
   in a product, an acknowledgment in the product documentation would be
   appreciated but is not required. 
2. Altered source versions must be plainly marked as such, and must not be
   misrepresented as being the original software.
3. This notice may not be removed or altered from any source distribution.
*/

package net.sourceforge.clonekeenplus;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.util.Log;
import java.io.*;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Environment;
import android.os.StatFs;
import java.util.Locale;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;
import java.util.Collections;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import java.lang.String;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.FrameLayout;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Button;
import android.view.View;
import android.widget.LinearLayout;
import android.text.Editable;
import android.text.SpannedString;
import android.content.Intent;
import android.app.PendingIntent;
import android.app.AlarmManager;
import android.util.DisplayMetrics;
import android.net.Uri;
import java.util.concurrent.Semaphore;
import java.util.Arrays;
import android.graphics.Color;
import android.hardware.SensorEventListener;
import android.hardware.SensorEvent;
import android.hardware.Sensor;
import android.widget.Toast;


class SettingsMenuKeyboard extends SettingsMenu
{
	static class KeyboardConfigMainMenu extends Menu
	{
		String title(final MainActivity p)
		{
			return p.getResources().getString(R.string.controls_screenkb);
		}
		boolean enabled()
		{
			return Globals.UseTouchscreenKeyboard;
		}
		void run (final MainActivity p)
		{
			Menu options[] =
			{
				new ScreenKeyboardThemeConfig(),
				new ScreenKeyboardSizeConfig(),
				new ScreenKeyboardDrawSizeConfig(),
				new ScreenKeyboardTransparencyConfig(),
				new RemapScreenKbConfig(),
				new CustomizeScreenKbLayout(),
				new ScreenKeyboardAdvanced(),
				new OkButton(),
			};
			showMenuOptionsList(p, options);
		}
	}
	
	static class ScreenKeyboardSizeConfig extends Menu
	{
		String title(final MainActivity p)
		{
			return p.getResources().getString(R.string.controls_screenkb_size);
		}
		void run (final MainActivity p)
		{
			final CharSequence[] items = {	p.getResources().getString(R.string.controls_screenkb_large),
											p.getResources().getString(R.string.controls_screenkb_medium),
											p.getResources().getString(R.string.controls_screenkb_small),
											p.getResources().getString(R.string.controls_screenkb_tiny),
											p.getResources().getString(R.string.controls_screenkb_custom) };

			AlertDialog.Builder builder = new AlertDialog.Builder(p);
			builder.setTitle(p.getResources().getString(R.string.controls_screenkb_size));
			builder.setSingleChoiceItems(items, Globals.TouchscreenKeyboardSize, new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int item)
				{
					Globals.TouchscreenKeyboardSize = item;
					dialog.dismiss();
					if( Globals.TouchscreenKeyboardSize == Globals.TOUCHSCREEN_KEYBOARD_CUSTOM )
						new CustomizeScreenKbLayout().run(p);
					else
						goBack(p);
				}
			});
			builder.setOnCancelListener(new DialogInterface.OnCancelListener()
			{
				public void onCancel(DialogInterface dialog)
				{
					goBack(p);
				}
			});
			AlertDialog alert = builder.create();
			alert.setOwnerActivity(p);
			alert.show();
		}
	}

	static class ScreenKeyboardDrawSizeConfig extends Menu
	{
		String title(final MainActivity p)
		{
			return p.getResources().getString(R.string.controls_screenkb_drawsize);
		}
		void run (final MainActivity p)
		{
			final CharSequence[] items = {	p.getResources().getString(R.string.controls_screenkb_large),
											p.getResources().getString(R.string.controls_screenkb_medium),
											p.getResources().getString(R.string.controls_screenkb_small),
											p.getResources().getString(R.string.controls_screenkb_tiny) };

			AlertDialog.Builder builder = new AlertDialog.Builder(p);
			builder.setTitle(p.getResources().getString(R.string.controls_screenkb_drawsize));
			builder.setSingleChoiceItems(items, Globals.TouchscreenKeyboardDrawSize, new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int item) 
				{
					Globals.TouchscreenKeyboardDrawSize = item;

					dialog.dismiss();
					goBack(p);
				}
			});
			builder.setOnCancelListener(new DialogInterface.OnCancelListener()
			{
				public void onCancel(DialogInterface dialog)
				{
					goBack(p);
				}
			});
			AlertDialog alert = builder.create();
			alert.setOwnerActivity(p);
			alert.show();
		}
	}

	static class ScreenKeyboardThemeConfig extends Menu
	{
		String title(final MainActivity p)
		{
			return p.getResources().getString(R.string.controls_screenkb_theme);
		}
		void run (final MainActivity p)
		{
			final CharSequence[] items = {
				p.getResources().getString(R.string.controls_screenkb_by, "Ultimate Droid", "Sean Stieber"),
				p.getResources().getString(R.string.controls_screenkb_by, "Simple Theme", "Beholder"),
				p.getResources().getString(R.string.controls_screenkb_by, "Sun", "Sirea"),
				p.getResources().getString(R.string.controls_screenkb_by, "Keen", "Gerstrong"),
				p.getResources().getString(R.string.controls_screenkb_by, "Retro", "Santiago Radeff")
				};

			AlertDialog.Builder builder = new AlertDialog.Builder(p);
			builder.setTitle(p.getResources().getString(R.string.controls_screenkb_theme));
			builder.setSingleChoiceItems(items, Globals.TouchscreenKeyboardTheme, new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int item) 
				{
					Globals.TouchscreenKeyboardTheme = item;

					dialog.dismiss();
					goBack(p);
				}
			});
			builder.setOnCancelListener(new DialogInterface.OnCancelListener()
			{
				public void onCancel(DialogInterface dialog)
				{
					goBack(p);
				}
			});
			AlertDialog alert = builder.create();
			alert.setOwnerActivity(p);
			alert.show();
		}
	}

	static class ScreenKeyboardTransparencyConfig extends Menu
	{
		String title(final MainActivity p)
		{
			return p.getResources().getString(R.string.controls_screenkb_transparency);
		}
		void run (final MainActivity p)
		{
			final CharSequence[] items = {	p.getResources().getString(R.string.controls_screenkb_trans_0),
											p.getResources().getString(R.string.controls_screenkb_trans_1),
											p.getResources().getString(R.string.controls_screenkb_trans_2),
											p.getResources().getString(R.string.controls_screenkb_trans_3),
											p.getResources().getString(R.string.controls_screenkb_trans_4) };

			AlertDialog.Builder builder = new AlertDialog.Builder(p);
			builder.setTitle(p.getResources().getString(R.string.controls_screenkb_transparency));
			builder.setSingleChoiceItems(items, Globals.TouchscreenKeyboardTransparency, new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int item) 
				{
					Globals.TouchscreenKeyboardTransparency = item;

					dialog.dismiss();
					goBack(p);
				}
			});
			builder.setOnCancelListener(new DialogInterface.OnCancelListener()
			{
				public void onCancel(DialogInterface dialog)
				{
					goBack(p);
				}
			});
			AlertDialog alert = builder.create();
			alert.setOwnerActivity(p);
			alert.show();
		}
	}
	
	static class RemapHwKeysConfig extends Menu
	{
		String title(final MainActivity p)
		{
			return p.getResources().getString(R.string.remap_hwkeys);
		}
		void run (final MainActivity p)
		{
			p.setText(p.getResources().getString(R.string.remap_hwkeys_press));
			p.keyListener = new KeyRemapTool(p);
		}

		public static class KeyRemapTool implements MainActivity.KeyEventsListener
		{
			MainActivity p;
			public KeyRemapTool(MainActivity _p)
			{
				p = _p;
			}
			
			public void onKeyEvent(final int keyCode)
			{
				p.keyListener = null;
				int keyIndex = keyCode;
				if( keyIndex < 0 )
					keyIndex = 0;
				if( keyIndex > SDL_Keys.JAVA_KEYCODE_LAST )
					keyIndex = 0;

				final int KeyIndexFinal = keyIndex;
				CharSequence[] items = {
					SDL_Keys.names[Globals.RemapScreenKbKeycode[0]],
					SDL_Keys.names[Globals.RemapScreenKbKeycode[1]],
					SDL_Keys.names[Globals.RemapScreenKbKeycode[2]],
					SDL_Keys.names[Globals.RemapScreenKbKeycode[3]],
					SDL_Keys.names[Globals.RemapScreenKbKeycode[4]],
					SDL_Keys.names[Globals.RemapScreenKbKeycode[5]],
					p.getResources().getString(R.string.remap_hwkeys_select_more_keys),
				};
				
				for( int i = 0; i < Math.min(6, Globals.AppTouchscreenKeyboardKeysNames.length); i++ )
					items[i] = Globals.AppTouchscreenKeyboardKeysNames[i].replace("_", " ");

				AlertDialog.Builder builder = new AlertDialog.Builder(p);
				builder.setTitle(R.string.remap_hwkeys_select_simple);
				builder.setItems(items, new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int item)
					{
						dialog.dismiss();
						if( item >= 6 )
							ShowAllKeys(KeyIndexFinal);
						else
						{
							Globals.RemapHwKeycode[KeyIndexFinal] = Globals.RemapScreenKbKeycode[item];
							goBack(p);
						}
					}
				});
				builder.setOnCancelListener(new DialogInterface.OnCancelListener()
				{
					public void onCancel(DialogInterface dialog)
					{
						goBack(p);
					}
				});
				AlertDialog alert = builder.create();
				alert.setOwnerActivity(p);
				alert.show();
			}
			public void ShowAllKeys(final int KeyIndex)
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(p);
				builder.setTitle(R.string.remap_hwkeys_select);
				builder.setSingleChoiceItems(SDL_Keys.namesSorted, SDL_Keys.namesSortedBackIdx[Globals.RemapHwKeycode[KeyIndex]], new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int item)
					{
						Globals.RemapHwKeycode[KeyIndex] = SDL_Keys.namesSortedIdx[item];

						dialog.dismiss();
						goBack(p);
					}
				});
				builder.setOnCancelListener(new DialogInterface.OnCancelListener()
				{
					public void onCancel(DialogInterface dialog)
					{
						goBack(p);
					}
				});
				AlertDialog alert = builder.create();
				alert.setOwnerActivity(p);
				alert.show();
			}
		}
	}

	static class RemapScreenKbConfig extends Menu
	{
		String title(final MainActivity p)
		{
			return p.getResources().getString(R.string.remap_screenkb);
		}
		//boolean enabled() { return true; };
		void run (final MainActivity p)
		{
			CharSequence[] items = {
				p.getResources().getString(R.string.remap_screenkb_joystick),
				p.getResources().getString(R.string.remap_screenkb_button_text),
				p.getResources().getString(R.string.remap_screenkb_button) + " 1",
				p.getResources().getString(R.string.remap_screenkb_button) + " 2",
				p.getResources().getString(R.string.remap_screenkb_button) + " 3",
				p.getResources().getString(R.string.remap_screenkb_button) + " 4",
				p.getResources().getString(R.string.remap_screenkb_button) + " 5",
				p.getResources().getString(R.string.remap_screenkb_button) + " 6",
			};

			boolean defaults[] = Arrays.copyOf(Globals.ScreenKbControlsShown, Globals.ScreenKbControlsShown.length);
			if( Globals.AppUsesSecondJoystick )
			{
				items = Arrays.copyOf(items, items.length + 1);
				items[items.length - 1] = p.getResources().getString(R.string.remap_screenkb_joystick) + " 2";
				defaults = Arrays.copyOf(defaults, defaults.length + 1);
				defaults[defaults.length - 1] = true;
			}
			if( Globals.AppUsesThirdJoystick )
			{
				items = Arrays.copyOf(items, items.length + 1);
				items[items.length - 1] = p.getResources().getString(R.string.remap_screenkb_joystick) + " 3";
				defaults = Arrays.copyOf(defaults, defaults.length + 1);
				defaults[defaults.length - 1] = true;
			}

			for( int i = 0; i < Math.min(6, Globals.AppTouchscreenKeyboardKeysNames.length); i++ )
				items[i+2] = items[i+2] + " - " + Globals.AppTouchscreenKeyboardKeysNames[i].replace("_", " ");

			AlertDialog.Builder builder = new AlertDialog.Builder(p);
			builder.setTitle(p.getResources().getString(R.string.remap_screenkb));
			builder.setMultiChoiceItems(items, defaults, new DialogInterface.OnMultiChoiceClickListener() 
			{
				public void onClick(DialogInterface dialog, int item, boolean isChecked) 
				{
					Globals.ScreenKbControlsShown[item] = isChecked;
				}
			});
			builder.setPositiveButton(p.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int item) 
				{
					dialog.dismiss();
					showRemapScreenKbConfig2(p, 0);
				}
			});
			builder.setOnCancelListener(new DialogInterface.OnCancelListener()
			{
				public void onCancel(DialogInterface dialog)
				{
					goBack(p);
				}
			});
			AlertDialog alert = builder.create();
			alert.setOwnerActivity(p);
			alert.show();
		}

		static void showRemapScreenKbConfig2(final MainActivity p, final int currentButton)
		{
			CharSequence[] items = {
				p.getResources().getString(R.string.remap_screenkb_button) + " 1",
				p.getResources().getString(R.string.remap_screenkb_button) + " 2",
				p.getResources().getString(R.string.remap_screenkb_button) + " 3",
				p.getResources().getString(R.string.remap_screenkb_button) + " 4",
				p.getResources().getString(R.string.remap_screenkb_button) + " 5",
				p.getResources().getString(R.string.remap_screenkb_button) + " 6",
			};

			for( int i = 0; i < Math.min(6, Globals.AppTouchscreenKeyboardKeysNames.length); i++ )
				items[i] = items[i] + " - " + Globals.AppTouchscreenKeyboardKeysNames[i].replace("_", " ");

			if( currentButton >= Globals.RemapScreenKbKeycode.length )
			{
				goBack(p);
				return;
			}
			if( ! Globals.ScreenKbControlsShown[currentButton + 2] )
			{
				showRemapScreenKbConfig2(p, currentButton + 1);
				return;
			}

			AlertDialog.Builder builder = new AlertDialog.Builder(p);
			builder.setTitle(items[currentButton]);
			builder.setSingleChoiceItems(SDL_Keys.namesSorted, SDL_Keys.namesSortedBackIdx[Globals.RemapScreenKbKeycode[currentButton]], new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int item)
				{
					Globals.RemapScreenKbKeycode[currentButton] = SDL_Keys.namesSortedIdx[item];

					dialog.dismiss();
					showRemapScreenKbConfig2(p, currentButton + 1);
				}
			});
			builder.setOnCancelListener(new DialogInterface.OnCancelListener()
			{
				public void onCancel(DialogInterface dialog)
				{
					goBack(p);
				}
			});
			AlertDialog alert = builder.create();
			alert.setOwnerActivity(p);
			alert.show();
		}
	}
	
	static class ScreenGesturesConfig extends Menu
	{
		String title(final MainActivity p)
		{
			return p.getResources().getString(R.string.remap_screenkb_button_gestures);
		}
		//boolean enabled() { return true; };
		void run (final MainActivity p)
		{
			CharSequence[] items = {
				p.getResources().getString(R.string.remap_screenkb_button_zoomin),
				p.getResources().getString(R.string.remap_screenkb_button_zoomout),
				p.getResources().getString(R.string.remap_screenkb_button_rotateleft),
				p.getResources().getString(R.string.remap_screenkb_button_rotateright),
			};

			boolean defaults[] = { 
				Globals.MultitouchGesturesUsed[0],
				Globals.MultitouchGesturesUsed[1],
				Globals.MultitouchGesturesUsed[2],
				Globals.MultitouchGesturesUsed[3],
			};
			
			AlertDialog.Builder builder = new AlertDialog.Builder(p);
			builder.setTitle(p.getResources().getString(R.string.remap_screenkb_button_gestures));
			builder.setMultiChoiceItems(items, defaults, new DialogInterface.OnMultiChoiceClickListener() 
			{
				public void onClick(DialogInterface dialog, int item, boolean isChecked) 
				{
					Globals.MultitouchGesturesUsed[item] = isChecked;
				}
			});
			builder.setPositiveButton(p.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int item) 
				{
					dialog.dismiss();
					showScreenGesturesConfig2(p);
				}
			});
			builder.setOnCancelListener(new DialogInterface.OnCancelListener()
			{
				public void onCancel(DialogInterface dialog)
				{
					goBack(p);
				}
			});
			AlertDialog alert = builder.create();
			alert.setOwnerActivity(p);
			alert.show();
		}

		static void showScreenGesturesConfig2(final MainActivity p)
		{
			final CharSequence[] items = {
				p.getResources().getString(R.string.accel_slow),
				p.getResources().getString(R.string.accel_medium),
				p.getResources().getString(R.string.accel_fast),
				p.getResources().getString(R.string.accel_veryfast)
			};

			AlertDialog.Builder builder = new AlertDialog.Builder(p);
			builder.setTitle(R.string.remap_screenkb_button_gestures_sensitivity);
			builder.setSingleChoiceItems(items, Globals.MultitouchGestureSensitivity, new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int item) 
				{
					Globals.MultitouchGestureSensitivity = item;

					dialog.dismiss();
					showScreenGesturesConfig3(p, 0);
				}
			});
			builder.setOnCancelListener(new DialogInterface.OnCancelListener()
			{
				public void onCancel(DialogInterface dialog)
				{
					goBack(p);
				}
			});
			AlertDialog alert = builder.create();
			alert.setOwnerActivity(p);
			alert.show();
		}

		static void showScreenGesturesConfig3(final MainActivity p, final int currentButton)
		{
			CharSequence[] items = {
				p.getResources().getString(R.string.remap_screenkb_button_zoomin),
				p.getResources().getString(R.string.remap_screenkb_button_zoomout),
				p.getResources().getString(R.string.remap_screenkb_button_rotateleft),
				p.getResources().getString(R.string.remap_screenkb_button_rotateright),
			};
			
			if( currentButton >= Globals.RemapMultitouchGestureKeycode.length )
			{
				goBack(p);
				return;
			}
			if( ! Globals.MultitouchGesturesUsed[currentButton] )
			{
				showScreenGesturesConfig3(p, currentButton + 1);
				return;
			}

			AlertDialog.Builder builder = new AlertDialog.Builder(p);
			builder.setTitle(items[currentButton]);
			builder.setSingleChoiceItems(SDL_Keys.namesSorted, SDL_Keys.namesSortedBackIdx[Globals.RemapMultitouchGestureKeycode[currentButton]], new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int item)
				{
					Globals.RemapMultitouchGestureKeycode[currentButton] = SDL_Keys.namesSortedIdx[item];

					dialog.dismiss();
					showScreenGesturesConfig3(p, currentButton + 1);
				}
			});
			builder.setOnCancelListener(new DialogInterface.OnCancelListener()
			{
				public void onCancel(DialogInterface dialog)
				{
					goBack(p);
				}
			});
			AlertDialog alert = builder.create();
			alert.setOwnerActivity(p);
			alert.show();
		}
	}

	static class CustomizeScreenKbLayout extends Menu
	{
		String title(final MainActivity p)
		{
			return p.getResources().getString(R.string.screenkb_custom_layout);
		}
		//boolean enabled() { return true; };
		void run (final MainActivity p)
		{
			p.setText(p.getResources().getString(R.string.screenkb_custom_layout_help));
			CustomizeScreenKbLayoutTool tool = new CustomizeScreenKbLayoutTool(p);
			p.touchListener = tool;
			p.keyListener = tool;
			Globals.TouchscreenKeyboardSize = Globals.TOUCHSCREEN_KEYBOARD_CUSTOM;
		}

		static class CustomizeScreenKbLayoutTool implements MainActivity.TouchEventsListener, MainActivity.KeyEventsListener
		{
			MainActivity p;
			FrameLayout layout = null;
			ImageView imgs[] = new ImageView[Globals.ScreenKbControlsLayout.length];
			Bitmap bmps[] = new Bitmap[Globals.ScreenKbControlsLayout.length];
			ImageView boundary = null;
			Bitmap boundaryBmp = null;
			int currentButton = 0;
			int buttons[] = {
				R.drawable.dpad,
				R.drawable.keyboard,
				R.drawable.b1,
				R.drawable.b2,
				R.drawable.b3,
				R.drawable.b4,
				R.drawable.b5,
				R.drawable.b6,
				R.drawable.dpad,
				R.drawable.dpad
			};
			int oldX = 0, oldY = 0;
			boolean resizing = false;
			
			public CustomizeScreenKbLayoutTool(MainActivity _p) 
			{
				p = _p;
				layout = new FrameLayout(p);
				p.getVideoLayout().addView(layout);
				boundary = new ImageView(p);
				boundary.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
				boundary.setScaleType(ImageView.ScaleType.MATRIX);
				boundaryBmp = BitmapFactory.decodeResource( p.getResources(), R.drawable.rectangle );
				boundary.setImageBitmap(boundaryBmp);
				layout.addView(boundary);
				currentButton = -1;
				if( Globals.TouchscreenKeyboardTheme == 2 )
				{
					buttons = new int[] {
						R.drawable.sun_dpad,
						R.drawable.sun_keyboard,
						R.drawable.sun_b1,
						R.drawable.sun_b2,
						R.drawable.sun_b3,
						R.drawable.sun_b4,
						R.drawable.sun_b5,
						R.drawable.sun_b6,
						R.drawable.sun_dpad,
						R.drawable.sun_dpad
					};
				}

				int displayX = 800;
				int displayY = 480;
				try {
					DisplayMetrics dm = new DisplayMetrics();
					p.getWindowManager().getDefaultDisplay().getMetrics(dm);
					displayX = dm.widthPixels;
					displayY = dm.heightPixels;
				} catch (Exception eeeee) {}

				for( int i = 0; i < Globals.ScreenKbControlsLayout.length; i++ )
				{
					if( ! Globals.ScreenKbControlsShown[i] )
						continue;
					if( currentButton == -1 )
						currentButton = i;
					//Log.i("SDL", "Screen kb button " + i + " coords " + Globals.ScreenKbControlsLayout[i][0] + ":" + Globals.ScreenKbControlsLayout[i][1] + ":" + Globals.ScreenKbControlsLayout[i][2] + ":" + Globals.ScreenKbControlsLayout[i][3] );
					// Check if the button is off screen edge or shrunk to zero
					if( Globals.ScreenKbControlsLayout[i][0] > Globals.ScreenKbControlsLayout[i][2] - displayY/12 )
						Globals.ScreenKbControlsLayout[i][0] = Globals.ScreenKbControlsLayout[i][2] - displayY/12;
					if( Globals.ScreenKbControlsLayout[i][1] > Globals.ScreenKbControlsLayout[i][3] - displayY/12 )
						Globals.ScreenKbControlsLayout[i][1] = Globals.ScreenKbControlsLayout[i][3] - displayY/12;
					if( Globals.ScreenKbControlsLayout[i][0] < Globals.ScreenKbControlsLayout[i][2] - displayY*2/3 )
						Globals.ScreenKbControlsLayout[i][0] = Globals.ScreenKbControlsLayout[i][2] - displayY*2/3;
					if( Globals.ScreenKbControlsLayout[i][1] < Globals.ScreenKbControlsLayout[i][3] - displayY*2/3 )
						Globals.ScreenKbControlsLayout[i][1] = Globals.ScreenKbControlsLayout[i][3] - displayY*2/3;
					if( Globals.ScreenKbControlsLayout[i][0] < 0 )
					{
						Globals.ScreenKbControlsLayout[i][2] += -Globals.ScreenKbControlsLayout[i][0];
						Globals.ScreenKbControlsLayout[i][0] = 0;
					}
					if( Globals.ScreenKbControlsLayout[i][2] > displayX )
					{
						Globals.ScreenKbControlsLayout[i][0] -= Globals.ScreenKbControlsLayout[i][2] - displayX;
						Globals.ScreenKbControlsLayout[i][2] = displayX;
					}
					if( Globals.ScreenKbControlsLayout[i][1] < 0 )
					{
						Globals.ScreenKbControlsLayout[i][3] += -Globals.ScreenKbControlsLayout[i][1];
						Globals.ScreenKbControlsLayout[i][1] = 0;
					}
					if( Globals.ScreenKbControlsLayout[i][3] > displayY )
					{
						Globals.ScreenKbControlsLayout[i][1] -= Globals.ScreenKbControlsLayout[i][3] - displayY;
						Globals.ScreenKbControlsLayout[i][3] = displayY;
					}
					//Log.i("SDL", "After bounds check coords " + Globals.ScreenKbControlsLayout[i][0] + ":" + Globals.ScreenKbControlsLayout[i][1] + ":" + Globals.ScreenKbControlsLayout[i][2] + ":" + Globals.ScreenKbControlsLayout[i][3] );

					imgs[i] = new ImageView(p);
					imgs[i].setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
					imgs[i].setScaleType(ImageView.ScaleType.MATRIX);
					bmps[i] = BitmapFactory.decodeResource( p.getResources(), buttons[i] );
					imgs[i].setImageBitmap(bmps[i]);
					imgs[i].setAlpha(128);
					layout.addView(imgs[i]);
					Matrix m = new Matrix();
					RectF src = new RectF(0, 0, bmps[i].getWidth(), bmps[i].getHeight());
					RectF dst = new RectF(Globals.ScreenKbControlsLayout[i][0], Globals.ScreenKbControlsLayout[i][1],
											Globals.ScreenKbControlsLayout[i][2], Globals.ScreenKbControlsLayout[i][3]);
					m.setRectToRect(src, dst, Matrix.ScaleToFit.FILL);
					imgs[i].setImageMatrix(m);
				}
				boundary.bringToFront();
				if( currentButton == -1 )
					onKeyEvent( KeyEvent.KEYCODE_BACK ); // All buttons disabled - do not show anything
				else
					setupButton(currentButton);
			}
			
			void setupButton(int i)
			{
				Matrix m = new Matrix();
				RectF src = new RectF(0, 0, bmps[i].getWidth(), bmps[i].getHeight());
				RectF dst = new RectF(Globals.ScreenKbControlsLayout[i][0], Globals.ScreenKbControlsLayout[i][1],
										Globals.ScreenKbControlsLayout[i][2], Globals.ScreenKbControlsLayout[i][3]);
				m.setRectToRect(src, dst, Matrix.ScaleToFit.FILL);
				imgs[i].setImageMatrix(m);
				m = new Matrix();
				src = new RectF(0, 0, boundaryBmp.getWidth(), boundaryBmp.getHeight());
				m.setRectToRect(src, dst, Matrix.ScaleToFit.FILL);
				boundary.setImageMatrix(m);
				String buttonText = "";
				if( i >= 2 && i <= 7 )
					buttonText = p.getResources().getString(R.string.remap_screenkb_button) + (i - 2);
				if( i >= 2 && i - 2 < Globals.AppTouchscreenKeyboardKeysNames.length )
					buttonText = Globals.AppTouchscreenKeyboardKeysNames[i - 2].replace("_", " ");
				if( i == 0 )
					buttonText = "Joystick";
				if( i == 1 )
					buttonText = "Text input";
				if( i == 8 )
					buttonText = "Joystick 2";
				if( i == 9 )
					buttonText = "Joystick 3";
				p.setText(p.getResources().getString(R.string.screenkb_custom_layout_help) + "\n" + buttonText);
			}

			public void onTouchEvent(final MotionEvent ev)
			{
				if( ev.getAction() == MotionEvent.ACTION_DOWN )
				{
					oldX = (int)ev.getX();
					oldY = (int)ev.getY();
					resizing = true;
					for( int i = 0; i < Globals.ScreenKbControlsLayout.length; i++ )
					{
						if( ! Globals.ScreenKbControlsShown[i] )
							continue;
						if( Globals.ScreenKbControlsLayout[i][0] <= oldX &&
							Globals.ScreenKbControlsLayout[i][2] >= oldX &&
							Globals.ScreenKbControlsLayout[i][1] <= oldY &&
							Globals.ScreenKbControlsLayout[i][3] >= oldY )
						{
							currentButton = i;
							setupButton(currentButton);
							resizing = false;
							break;
						}
					}
				}
				if( ev.getAction() == MotionEvent.ACTION_MOVE )
				{
					int dx = (int)ev.getX() - oldX;
					int dy = (int)ev.getY() - oldY;
					if( resizing )
					{
						// Resize slowly, with 1/3 of movement speed
						dx /= 6;
						dy /= 6;
						if( Globals.ScreenKbControlsLayout[currentButton][0] <= Globals.ScreenKbControlsLayout[currentButton][2] + dx*2 )
						{
							Globals.ScreenKbControlsLayout[currentButton][0] -= dx;
							Globals.ScreenKbControlsLayout[currentButton][2] += dx;
						}
						if( Globals.ScreenKbControlsLayout[currentButton][1] <= Globals.ScreenKbControlsLayout[currentButton][3] + dy*2 )
						{
							Globals.ScreenKbControlsLayout[currentButton][1] += dy;
							Globals.ScreenKbControlsLayout[currentButton][3] -= dy;
						}
						dx *= 6;
						dy *= 6;
					}
					else
					{
						Globals.ScreenKbControlsLayout[currentButton][0] += dx;
						Globals.ScreenKbControlsLayout[currentButton][2] += dx;
						Globals.ScreenKbControlsLayout[currentButton][1] += dy;
						Globals.ScreenKbControlsLayout[currentButton][3] += dy;
					}
					oldX += dx;
					oldY += dy;
					Matrix m = new Matrix();
					RectF src = new RectF(0, 0, bmps[currentButton].getWidth(), bmps[currentButton].getHeight());
					RectF dst = new RectF(Globals.ScreenKbControlsLayout[currentButton][0], Globals.ScreenKbControlsLayout[currentButton][1],
											Globals.ScreenKbControlsLayout[currentButton][2], Globals.ScreenKbControlsLayout[currentButton][3]);
					m.setRectToRect(src, dst, Matrix.ScaleToFit.FILL);
					imgs[currentButton].setImageMatrix(m);
					m = new Matrix();
					src = new RectF(0, 0, boundaryBmp.getWidth(), boundaryBmp.getHeight());
					m.setRectToRect(src, dst, Matrix.ScaleToFit.FILL);
					boundary.setImageMatrix(m);
				}
			}

			public void onKeyEvent(final int keyCode)
			{
				if( keyCode == KeyEvent.KEYCODE_BACK )
				{
					p.getVideoLayout().removeView(layout);
					layout = null;
					p.touchListener = null;
					p.keyListener = null;
					goBack(p);
				}
			}
		}
	}

	static class ScreenKeyboardAdvanced extends Menu
	{
		String title(final MainActivity p)
		{
			return p.getResources().getString(R.string.advanced);
		}
		//boolean enabled() { return true; };
		void run (final MainActivity p)
		{
			CharSequence[] items = {
				p.getResources().getString(R.string.screenkb_floating_joystick),
			};

			boolean defaults[] = { 
				Globals.FloatingScreenJoystick,
			};
			
			AlertDialog.Builder builder = new AlertDialog.Builder(p);
			builder.setTitle(p.getResources().getString(R.string.advanced));
			builder.setMultiChoiceItems(items, defaults, new DialogInterface.OnMultiChoiceClickListener() 
			{
				public void onClick(DialogInterface dialog, int item, boolean isChecked) 
				{
					if( item == 0 )
						Globals.FloatingScreenJoystick = isChecked;
				}
			});
			builder.setPositiveButton(p.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int item) 
				{
					dialog.dismiss();
					goBack(p);
				}
			});
			builder.setOnCancelListener(new DialogInterface.OnCancelListener()
			{
				public void onCancel(DialogInterface dialog)
				{
					goBack(p);
				}
			});
			AlertDialog alert = builder.create();
			alert.setOwnerActivity(p);
			alert.show();
		}
	}
}

