import { zodResolver } from '@hookform/resolvers/zod';
import { ArrowBack, Logout } from '@mui/icons-material';
import { Box, Button, Divider, Link, Paper, Stack, Typography } from '@mui/material';
import { useForm } from 'react-hook-form';
import { useEffect } from 'react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { authApi, meApi } from '../../services/api';
import { session } from '../../services/session';
import { Link as RouterLink, useNavigate } from 'react-router-dom';
import { FormSelect, FormTextField } from '../../components/ui';
import {
  profileSchema,
  signInSchema,
  signUpSchema,
  type ProfileValues,
  type SignInValues,
  type SignUpValues,
} from '../shared/schemas';

function AuthCard({ children }: { children: React.ReactNode }) {
  return (
    <Paper sx={{ width: 'min(100%, 460px)', p: { xs: 3, sm: 5 }, borderRadius: 4 }}>
      {children}
    </Paper>
  );
}
export function SignInPage() {
  const navigate = useNavigate();
  const signIn = useMutation({
    mutationFn: authApi.login,
    onSuccess: (value) => {
      session.set(value);
      navigate('/dashboard');
    },
  });
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<SignInValues>({ resolver: zodResolver(signInSchema) });
  return (
    <AuthCard>
      <Typography variant="h4">Welcome back</Typography>
      <Typography color="text.secondary" mt={1}>
        Sign in to prepare your next ride.
      </Typography>
      <Stack
        component="form"
        onSubmit={handleSubmit((values) =>
          signIn.mutate({ email: values.email, passwordHash: values.password }),
        )}
        spacing={2.25}
        mt={4}
      >
        <FormTextField
          name="email"
          label="Email address"
          type="email"
          register={register}
          errors={errors}
          required
        />
        <FormTextField
          name="password"
          label="Password"
          type="password"
          register={register}
          errors={errors}
          required
        />
        <Button type="submit" variant="contained" size="large" disabled={signIn.isPending}>
          Sign in
        </Button>
        {signIn.error && (
          <Typography color="error">{(signIn.error as { message: string }).message}</Typography>
        )}
      </Stack>
      <Divider sx={{ my: 3 }} />
      <Typography align="center" color="text.secondary">
        New here?{' '}
        <Link component={RouterLink} to="/sign-up" fontWeight={800}>
          Create an account
        </Link>
      </Typography>
    </AuthCard>
  );
}
export function SignUpPage() {
  const navigate = useNavigate();
  const signUp = useMutation({
    mutationFn: authApi.signUp,
    onSuccess: (value) => {
      session.set(value);
      navigate('/dashboard');
    },
  });
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<SignUpValues>({ resolver: zodResolver(signUpSchema) });
  return (
    <AuthCard>
      <Typography variant="h4">Start your journey</Typography>
      <Typography color="text.secondary" mt={1}>
        Create your rider profile in a few moments.
      </Typography>
      <Stack
        component="form"
        onSubmit={handleSubmit((values) =>
          signUp.mutate({
            firstName: values.firstName,
            lastName: values.lastName,
            email: values.email,
            passwordHash: values.password,
          }),
        )}
        spacing={2.1}
        mt={4}
      >
        <Stack direction={{ sm: 'row' }} spacing={2}>
          <FormTextField
            name="firstName"
            label="First name"
            register={register}
            errors={errors}
            required
          />
          <FormTextField
            name="lastName"
            label="Last name"
            register={register}
            errors={errors}
            required
          />
        </Stack>
        <FormTextField
          name="email"
          label="Email address"
          type="email"
          register={register}
          errors={errors}
          required
        />
        <FormTextField
          name="password"
          label="Password"
          type="password"
          register={register}
          errors={errors}
          required
        />
        <FormTextField
          name="confirmPassword"
          label="Confirm password"
          type="password"
          register={register}
          errors={errors}
          required
        />
        <Button type="submit" variant="contained" size="large" disabled={signUp.isPending}>
          Create account
        </Button>
        {signUp.error && (
          <Typography color="error">{(signUp.error as { message: string }).message}</Typography>
        )}
      </Stack>
      <Typography align="center" color="text.secondary" mt={3}>
        Already a member?{' '}
        <Link component={RouterLink} to="/sign-in" fontWeight={800}>
          Sign in
        </Link>
      </Typography>
    </AuthCard>
  );
}
export function ProfilePage() {
  const queryClient = useQueryClient();
  const profile = useQuery({ queryKey: ['profile'], queryFn: meApi.profile });
  const {
    register,
    handleSubmit,
    control,
    formState: { errors },
    reset,
  } = useForm<ProfileValues>({
    resolver: zodResolver(profileSchema),
    defaultValues: {
      firstName: 'Alex',
      lastName: 'Martin',
      licenseType: 'A',
      licenseYear: 2018,
      experience: 'INTERMEDIATE',
      primaryUsage: 'LEISURE',
      annualDistance: 7000,
    },
  });
  useEffect(() => {
    if (profile.data)
      reset({
        firstName: profile.data.firstName,
        lastName: profile.data.lastName,
        licenseType: profile.data.licenseType ?? 'A',
        licenseYear: profile.data.licenseYear ?? new Date().getFullYear(),
        experience: profile.data.experienceLevel ?? 'BEGINNER',
        primaryUsage: profile.data.primaryUsage ?? 'LEISURE',
        annualDistance: profile.data.estimatedAnnualDistance ?? 0,
      });
  }, [profile.data, reset]);
  const save = useMutation({
    mutationFn: (values: ProfileValues) =>
      meApi.updateProfile({
        firstName: values.firstName,
        lastName: values.lastName,
        licenseType: values.licenseType,
        licenseYear: values.licenseYear,
        experienceLevel: values.experience,
        primaryUsage: values.primaryUsage,
        estimatedAnnualDistance: values.annualDistance,
      }),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['profile'] }),
  });
  return (
    <Box className="page-content">
      <Typography color="primary" fontWeight={800} variant="overline">
        Account
      </Typography>
      <Typography variant="h3" mb={4}>
        Rider profile
      </Typography>
      <Paper sx={{ maxWidth: 760, p: { xs: 2, sm: 4 }, borderRadius: 3 }}>
        <Stack
          component="form"
          spacing={2.5}
          onSubmit={handleSubmit((values) => save.mutate(values))}
        >
          <Stack direction={{ sm: 'row' }} spacing={2}>
            <FormTextField
              name="firstName"
              label="First name"
              register={register}
              errors={errors}
              required
            />
            <FormTextField
              name="lastName"
              label="Last name"
              register={register}
              errors={errors}
              required
            />
          </Stack>
          <Stack direction={{ sm: 'row' }} spacing={2}>
            <FormSelect
              name="licenseType"
              label="License type"
              values={['A1', 'A2', 'A']}
              control={control}
              errors={errors}
            />
            <FormTextField
              name="licenseYear"
              label="License year"
              type="number"
              register={register}
              errors={errors}
              required
            />
          </Stack>
          <Stack direction={{ sm: 'row' }} spacing={2}>
            <FormSelect
              name="experience"
              label="Experience level"
              values={['BEGINNER', 'INTERMEDIATE', 'EXPERIENCED']}
              control={control}
              errors={errors}
            />
            <FormSelect
              name="primaryUsage"
              label="Primary usage"
              values={['COMMUTING', 'LEISURE', 'TRAVEL', 'MIXED']}
              control={control}
              errors={errors}
            />
          </Stack>
          <FormTextField
            name="annualDistance"
            label="Estimated annual distance (km)"
            type="number"
            register={register}
            errors={errors}
            required
          />
          <Button type="submit" variant="contained" sx={{ alignSelf: 'flex-start' }}>
            Save changes
          </Button>
        </Stack>
      </Paper>
    </Box>
  );
}
export function SignOutPage() {
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const signOut = useMutation({
    mutationFn: authApi.logout,
    onSettled: () => {
      session.clear();
      queryClient.clear();
      navigate('/sign-in');
    },
  });
  return (
    <Box className="page-content" sx={{ display: 'grid', placeItems: 'center', minHeight: '80vh' }}>
      <Paper sx={{ maxWidth: 460, p: 5, textAlign: 'center', borderRadius: 4 }}>
        <Logout color="primary" sx={{ fontSize: 44 }} />
        <Typography variant="h4" mt={2}>
          Sign out?
        </Typography>
        <Typography color="text.secondary" mt={1}>
          You can return any time to plan the next adventure.
        </Typography>
        <Stack direction="row" spacing={1.5} justifyContent="center" mt={4}>
          <Button startIcon={<ArrowBack />} onClick={() => navigate(-1)}>
            Stay signed in
          </Button>
          <Button variant="contained" onClick={() => signOut.mutate()}>
            Sign out
          </Button>
        </Stack>
      </Paper>
    </Box>
  );
}
